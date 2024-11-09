package rss.reader.templates

import io.ktor.server.html.*
import kotlinx.html.*

class LoginTemplate : Template<HTML> {
    override fun HTML.apply() {
        head {
            title { +"Login" }
            link(rel = "stylesheet", href = "/static/css/output.css", type = "text/css")
        }
        body(classes = "bg-gray-100 dark:bg-gray-900 flex items-center justify-center min-h-screen") {
            div(classes = "bg-white dark:bg-gray-800 p-6 rounded shadow-md") {
                h2(classes = "text-2xl font-bold mb-4 text-gray-800 dark:text-gray-200") { +"Login" }
                form(
                    action = "/login",
                    encType = FormEncType.applicationXWwwFormUrlEncoded,
                    method = FormMethod.post,
                    classes = "w-full"
                ) {
                    div(classes = "mb-4") {
                        label(classes = "block text-gray-700 dark:text-gray-300") {
                            attributes["for"] = "username"
                            +"Username"
                        }
                        input(
                            type = InputType.text,
                            name = "username",
                            classes = "w-full px-4 py-2 border rounded-lg dark:bg-gray-700 dark:border-gray-600 dark:text-gray-200"
                        ) {
                            attributes["id"] = "username"
                            required = true
                        }
                    }
                    div(classes = "mb-4") {
                        label(classes = "block text-gray-700 dark:text-gray-300") {
                            attributes["for"] = "password"
                            +"Password"
                        }
                        input(
                            type = InputType.password,
                            name = "password",
                            classes = "w-full px-4 py-2 border rounded-lg dark:bg-gray-700 dark:border-gray-600 dark:text-gray-200"
                        ) {
                            attributes["id"] = "password"
                            required = true
                        }
                    }
                    button(
                        type = ButtonType.submit,
                        classes = "w-full bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 dark:bg-blue-600 dark:hover:bg-blue-700"
                    ) {
                        +"Log In"
                    }
                }
                div(classes = "mt-4 text-center") {
                    p(classes = "text-gray-700 dark:text-gray-300") {
                        a(href = "/register", classes = "text-blue-500 dark:text-blue-400 hover:underline") {
                            +"Don't have an account? Register here"
                        }
                    }
                }
            }
        }
    }
}