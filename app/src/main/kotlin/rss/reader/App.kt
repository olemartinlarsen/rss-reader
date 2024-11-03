package rss.reader

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(Sessions) {
            cookie<UserSession>("user_session") {
                cookie.path = "/"
                cookie.httpOnly = true
            }
        }

        install(Authentication) {
            form("auth-form") {
                userParamName = "username"
                passwordParamName = "password"
                validate { credentials ->
                    if (credentials.name == "jetbrains" && credentials.password == "foobar") {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
                challenge {
                    call.respond(HttpStatusCode.Unauthorized, "Credentials are not valid")
                }
            }
            session<UserSession>("auth-session") {
                validate { session ->
                    if (session.name.isNotEmpty()) session else null
                }
                challenge {
                    call.respondRedirect("/login")
                }
            }
        }

        routing {
            staticResources("/static", "static")

            route("/login") {
                get {
                    call.respondHtmlTemplate(LoginTemplate()) {}
                }

                authenticate("auth-form") {
                    post {
                        val principal = call.principal<UserIdPrincipal>()
                        if (principal != null) {
                            call.sessions.set(UserSession(name = principal.name, count = 1))
                            call.respondRedirect("/")
                        } else {
                            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                        }
                    }
                }
            }

            authenticate("auth-session") {
                route("/") {
                    get {
                        val userSession = call.sessions.get<UserSession>()
                        if (userSession != null) {
                            call.respondHtmlTemplate(DashboardTemplate()) {
                                name {
                                    +userSession.name
                                }
                            }
                        } else {
                            call.respondRedirect("/login")
                        }
                    }
                }
            }
        }
    }.start(wait = true)
}

@Serializable
data class UserSession(val name: String, val count: Int)