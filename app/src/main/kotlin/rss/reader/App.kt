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
import rss.reader.database.initDatabase
import rss.reader.routes.authRoutes
import rss.reader.services.UserService
import rss.reader.services.AuthService

fun main() {
    val database = initDatabase()
    val userService = UserService(database)
    val authService = AuthService(userService)

    embeddedServer(Netty, port = 8080) {
        install(Sessions) {
            cookie<UserSession>("user_session") {
                cookie.path = "/"
                cookie.httpOnly = true
            }
        }

        install(Authentication) {
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

            authRoutes(authService)
        }
    }.start(wait = true)
}