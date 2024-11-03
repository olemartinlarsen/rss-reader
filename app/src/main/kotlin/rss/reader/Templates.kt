package rss.reader

import io.ktor.server.html.*
import kotlinx.html.*

class LoginTemplate : Template<HTML> {
    override fun HTML.apply() {
        body {
            form(
                action = "/login",
                encType = FormEncType.applicationXWwwFormUrlEncoded,
                method = FormMethod.post
            ) {
                p { +"Username:"; textInput(name = "username") }
                p { +"Password:"; passwordInput(name = "password") }
                p { submitInput { value = "Login" } }
            }
        }
    }
}

class DashboardTemplate : Template<HTML> {
    val name = Placeholder<FlowContent>()
    override fun HTML.apply() {
        body {
            h1 {
                +"Hello "
                insert(name)
            }
        }
    }
}