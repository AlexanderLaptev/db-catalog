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
        staticResources("/scripts", "scripts")
        staticResources("/styles", "styles")

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

private fun Route.tablePageRoute(type: String, title: String) {
    route("/$type/view") {
        get {
            call.respond(
                ThymeleafContent(
                    "table",
                    mapOf(
                        "title" to title,
                        "type" to type,
                    )
                )
            )
        }
    }
}

// TODO: cleanup!
private fun Route.editRoutes() {
    editPageRoute("medicine", "Medicine", MedicineRepository)
    editPageRoute("company", "Company", CompanyRepository)
    editPageRoute("pharmacy", "Pharmacy", PharmacyRepository)
}

private fun Route.editPageRoute(type: String, name: String, repository: AbstractRepository<*>) {
    route("/$type/edit") {
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
                    "/edit/base",
                    mapOf(
                        "verb" to "edit",
                        "type" to type,
                        "name" to name,
                    )
                )
            )
        }
    }
}

private fun Route.deleteRoutes() {
    deleteRoute("medicine", MedicineRepository)
    deleteRoute("company", CompanyRepository)
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
    addRoute("medicine", "Medicine")
    addRoute("company", "Company")
    addRoute("pharmacy", "Pharmacy")
}

private fun Route.addRoute(type: String, name: String) {
    route("/$type/add") {
        get {
            call.respond(
                ThymeleafContent(
                    "/edit/base",
                    mapOf(
                        "verb" to "add",
                        "type" to type,
                        "name" to name,
                    )
                )
            )
        }
    }
}
