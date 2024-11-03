package rss.reader

import io.ktor.server.html.*
import kotlinx.html.*

class LoginTemplate : Template<HTML> {
    override fun HTML.apply() {
        head {
            title { +"Login" }
            link(rel = "stylesheet", href = "/static/css/output.css", type = "text/css")
        }
        body(classes = "bg-gray-100 flex items-center justify-center min-h-screen") {
            div(classes = "bg-white p-6 rounded shadow-md") {
                h2(classes = "text-2xl font-bold mb-4") { +"Login" }
                form(
                    action = "/login",
                    encType = FormEncType.applicationXWwwFormUrlEncoded,
                    method = FormMethod.post,
                    classes = "w-full"
                ) {
                    div(classes = "mb-4") {
                        label(classes = "block text-gray-700") {
                            attributes["for"] = "username"
                            +"Username"
                        }
                        input(
                            type = InputType.text,
                            name = "username",
                            classes = "w-full px-4 py-2 border rounded-lg"
                        ) {
                            attributes["id"] = "username"
                            required = true
                        }
                    }
                    div(classes = "mb-4") {
                        label(classes = "block text-gray-700") {
                            attributes["for"] = "password"
                            +"Password"
                        }
                        input(
                            type = InputType.password,
                            name = "password",
                            classes = "w-full px-4 py-2 border rounded-lg"
                        ) {
                            attributes["id"] = "password"
                            required = true
                        }
                    }
                    button(
                        type = ButtonType.submit,
                        classes = "w-full bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
                    ) {
                        +"Log In"
                    }
                }
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