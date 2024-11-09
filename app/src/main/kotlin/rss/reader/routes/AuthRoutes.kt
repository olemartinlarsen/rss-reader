package rss.reader.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import rss.reader.auth.UserSession
import rss.reader.services.AuthService
import rss.reader.templates.DashboardTemplate
import rss.reader.templates.LoginTemplate
import rss.reader.templates.RegisterTemplate

fun Route.authRoutes(authService: AuthService) {
    route("/login") {
        get {
            call.respondHtmlTemplate(LoginTemplate()) {}
        }

        post {
            val formParameters = call.receiveParameters()
            val username = formParameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing username")
            val password = formParameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing password")

            val isAuthenticated = authService.authenticateUser(username, password)
            if (isAuthenticated) {
                call.sessions.set(UserSession(name = username))
                call.respondRedirect("/")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }
    }

    route("/register") {
        get {
            call.respondHtmlTemplate(RegisterTemplate()) {}
        }

        post {
            val formParameters = call.receiveParameters()
            val username = formParameters["username"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing username")
            val password = formParameters["password"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing password")
            val confirmPassword = formParameters["confirm-password"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing confirm password")

            if (password != confirmPassword) {
                call.respond(HttpStatusCode.BadRequest, "Passwords do not match")
                return@post
            }

            val isRegistered = authService.registerUser(username, password)
            if (isRegistered) {
                call.respondRedirect("/login")
            } else {
                call.respond(HttpStatusCode.Conflict, "User already exists")
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