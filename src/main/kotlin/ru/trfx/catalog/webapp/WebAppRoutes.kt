package ru.trfx.catalog.webapp

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.company.CompanyRepository
import ru.trfx.catalog.medicine.MedicineRepository
import ru.trfx.catalog.pharmacy.PharmacyRepository
import ru.trfx.catalog.repository.AbstractRepository

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

// TODO: cleanup!
private fun Route.editRoutes() {
    editPageRoute("company", CompanyRepository)
    editPageRoute("medicine", MedicineRepository)
    editPageRoute("pharmacy", PharmacyRepository)
}

private fun Route.editPageRoute(pathRoot: String, repository: AbstractRepository<*>) {
    route("/$pathRoot/edit") {
        get {
            val id = call.queryParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val exists = transaction { repository.existsById(id) }
            if (!exists) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(
                ThymeleafContent(
                    "/edit/$pathRoot",
                    mapOf(
                        "root" to "edit",
                        "script" to pathRoot,
                    )
                )
            )
        }
    }
}

private fun Route.deleteRoutes() {
    deleteRoute("company", CompanyRepository)
    deleteRoute("medicine", MedicineRepository)
    deleteRoute("pharmacy", PharmacyRepository)
}

private fun Route.deleteRoute(pathRoot: String, repository: AbstractRepository<*>) {
    route("/$pathRoot/delete") {
        get {
            val id = call.queryParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val result = transaction { repository.deleteById(id) }
            if (!result) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respondRedirect("/$pathRoot/view")
        }
    }
}

private fun Route.addRoutes() {
    addRoute("company")
    addRoute("medicine")
    addRoute("pharmacy")
}

private fun Route.addRoute(pathRoot: String) {
    route("/$pathRoot/add") {
        get {
            call.respond(
                ThymeleafContent(
                    "/edit/$pathRoot",
                    mapOf(
                        "root" to "add",
                        "script" to pathRoot,
                    )
                )
            )
        }
    }
}
