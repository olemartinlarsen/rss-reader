package rss.reader

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import rss.reader.auth.UserSession
import rss.reader.routes.authRoutes

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
            authRoutes()
        }
    }.start(wait = true)
}