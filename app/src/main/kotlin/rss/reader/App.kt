package rss.reader

import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080) {
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
        }

        routing {
            authenticate("auth-form") {
                post("/login") {
                    call.respondRedirect("/dashboard")
                }
            }
        }
    }.start(wait = true)
}