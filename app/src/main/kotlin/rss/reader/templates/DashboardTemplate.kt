package rss.reader.templates

import io.ktor.server.html.*
import kotlinx.html.*

class DashboardTemplate : Template<HTML> {
    val name = Placeholder<FlowContent>()

    override fun HTML.apply() {
        head {
            title { +"Dashboard" }
            link(rel = "stylesheet", href = "/static/css/output.css", type = "text/css")
        }
        body(classes = "bg-gray-100 dark:bg-gray-900 font-sans leading-normal tracking-normal") {
            nav(classes = "bg-blue-500 dark:bg-gray-800 p-4") {
                div(classes = "container mx-auto flex justify-between items-center") {
                    h1(classes = "text-white dark:text-gray-200 text-2xl font-bold") { +"Dashboard" }
                    div {
                        a(href = "/logout", classes = "text-white dark:text-gray-300 hover:text-gray-200") { +"Logout" }
                    }
                }
            }

            div(classes = "container mx-auto mt-8") {
                div(classes = "bg-white dark:bg-gray-800 shadow-md rounded-lg p-6") {
                    h2(classes = "text-2xl font-semibold mb-4 text-gray-800 dark:text-gray-200") {
                        +"Welcome, "
                        insert(name)
                        +"!"
                    }
                }
            }
        }
    }
}