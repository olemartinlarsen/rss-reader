package rss.reader

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondText(App().greeting)  // Respond with "Hello World!"
            }
        }
    }.start(wait = true)
}