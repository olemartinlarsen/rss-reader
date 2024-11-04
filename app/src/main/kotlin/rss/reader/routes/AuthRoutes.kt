package rss.reader.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import rss.reader.auth.UserSession
import rss.reader.templates.DashboardTemplate
import rss.reader.templates.LoginTemplate

fun Route.authRoutes() {
    route("/login") {
        get {
            call.respondHtmlTemplate(LoginTemplate()) {}
        }

        authenticate("auth-form") {
            post {
                val principal = call.principal<UserIdPrincipal>()
                if (principal != null) {
                    call.sessions.set(UserSession(name = principal.name))
                    call.respondRedirect("/")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                }
            }
        }
    }

    get("/logout") {
        call.sessions.clear<UserSession>()
        call.respondRedirect("/login")
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