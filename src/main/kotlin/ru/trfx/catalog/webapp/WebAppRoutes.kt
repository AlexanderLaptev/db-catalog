package ru.trfx.catalog.webapp

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.company.CompanyRepository

fun Application.webAppRoutes() {
    routing {
        staticResources("/scripts", "js")
        staticResources("/styles", "css")

        indexRoute()
        tableRoutes()

        editRoutes()
        deleteRoutes()
        addRoutes()
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
    tablePageRoute("medicine", "Medicines")
    tablePageRoute("company", "Companies")
    tablePageRoute("pharmacy", "Pharmacies")
}

private fun Route.tablePageRoute(pathRoot: String, title: String) {
    route("/$pathRoot/view") {
        get {
            call.respond(
                ThymeleafContent(
                    "table",
                    mapOf(
                        "title" to title,
                        "path" to pathRoot,
                        "initScript" to "/scripts/init/${pathRoot}.js"
                    )
                )
            )
        }
    }
}

private fun Route.editRoutes() {
    route("/company/edit") {
        get {
            val id = call.queryParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val exists = transaction { CompanyRepository.existsById(id) }
            if (!exists) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(ThymeleafContent("/edit/company", mapOf()))
        }
    }
}

private fun Route.deleteRoutes() {
    route("/company/delete") {
        get {
            val id = call.queryParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val result = transaction { CompanyRepository.deleteById(id) }
            if (!result) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respondRedirect("/company/view")
        }
    }
}

private fun Route.addRoutes() {
}
