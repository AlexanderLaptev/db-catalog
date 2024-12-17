package ru.trfx.catalog.webapp

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*

fun Application.webAppRoutes() {
    routing {
        staticResources("/scripts", "js")
        staticResources("/styles", "css")

        indexRoute()
        tableRoutes()
    }
}

private fun Route.indexRoute() {
    route("/") {
        get {
            call.respond(ThymeleafContent("welcome", emptyMap()))
        }
    }
}

private fun Route.tableRoutes() {
    tablePageRoute("medicines", "Medicines")
    tablePageRoute("companies", "Companies")
    tablePageRoute("pharmacies", "Pharmacies")
}

private fun Route.tablePageRoute(pathRoot: String, title: String) {
    route("/$pathRoot") {
        get {
            call.respond(
                ThymeleafContent(
                    "table", mapOf(
                        "title" to title,
                        "initScript" to "/scripts/init/${pathRoot}.js"
                    )
                )
            )
        }
    }
}
