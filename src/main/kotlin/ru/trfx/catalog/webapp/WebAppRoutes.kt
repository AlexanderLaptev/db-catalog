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
        medicineRoute()
    }
}

private fun Route.indexRoute() {
    route("/") {
        get {
            call.respond(ThymeleafContent("welcome", emptyMap()))
        }
    }
}

private fun Route.medicineRoute() {
    route("/medicines") {
        get {
            call.respond(
                ThymeleafContent(
                    "table",
                    mapOf(
                        "title" to "Medicines",
                        "initScript" to "/scripts/init/medicines.js",
                    )
                )
            )
        }
    }

    route("/companies") {
        get {
            call.respond(
                ThymeleafContent(
                    "table",
                    mapOf(
                        "title" to "Companies",
                        "initScript" to "/scripts/init/companies.js"
                    )
                )
            )
        }
    }
}

private fun getPageContent(path: String): String = object {}.javaClass.getResource(path)!!.readText()
