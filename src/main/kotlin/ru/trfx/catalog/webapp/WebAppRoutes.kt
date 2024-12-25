package ru.trfx.catalog.webapp

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.company.CompanyRepository
import ru.trfx.catalog.manufacturer.MedicineManufacturerRepository
import ru.trfx.catalog.medicine.MedicineRepository
import ru.trfx.catalog.pharmacy.PharmacyRepository
import ru.trfx.catalog.repository.AbstractRepository
import ru.trfx.catalog.response.ErrorResponse
import ru.trfx.catalog.search.medicineSearchRoutes
import ru.trfx.catalog.stock.StockRepository

fun Application.webAppRoutes() {
    routing {
        staticResources("/scripts", "scripts")
        staticResources("/styles", "styles")

        indexRoute()
        medicineSearchRoutes()

        tableRoutes()
        editRoutes()
        deleteRoutes()
        addRoutes()
    }
}

private fun Route.indexRoute() {
    route("/") {
        get {
            call.respond(ThymeleafContent("welcome", mapOf()))
        }
    }
}

private fun Route.tableRoutes() {
    tablePageRoute("medicine", "Medicines")
    tablePageRoute("company", "Companies")
    tablePageRoute("pharmacy", "Pharmacies")
    tablePageRoute("manufacturer", "Manufacturers")
    tablePageRoute("stock", "Stocks")
}

private fun Route.tablePageRoute(type: String, title: String) {
    get("/$type/view") {
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

private fun Route.editRoutes() {
    editPageRoute("medicine", "Medicine", MedicineRepository)
    editPageRoute("company", "Company", CompanyRepository)
    editPageRoute("pharmacy", "Pharmacy", PharmacyRepository)

    editManufacturerRoute()
    editStockRoute()
}

private fun Route.editPageRoute(type: String, name: String, repository: AbstractRepository<*>) {
    get("/$type/edit") {
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

private fun Route.editManufacturerRoute() {
    get("/manufacturer/edit") {
        val medicineId = call.queryParameters["medicine"]!!.toLongOrNull()
        if (medicineId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        val companyId = call.queryParameters["company"]!!.toLongOrNull()
        if (companyId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        val exists = transaction { MedicineManufacturerRepository.hasManufacturer(medicineId, companyId) }
        if (!exists) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        call.respond(
            ThymeleafContent(
                "/edit/base",
                mapOf(
                    "verb" to "edit",
                    "type" to "manufacturer",
                    "name" to "Manufacturer",
                )
            )
        )
    }
}

private fun Route.editStockRoute() {
    get("/stock/edit") {
        val medicineId = call.queryParameters["medicine"]!!.toLongOrNull()
        if (medicineId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        val pharmacy = call.queryParameters["pharmacy"]!!.toLongOrNull()
        if (pharmacy == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        val exists = transaction { StockRepository.find(medicineId, pharmacy) != null }
        if (!exists) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        call.respond(
            ThymeleafContent(
                "/edit/base",
                mapOf(
                    "verb" to "edit",
                    "type" to "stock",
                    "name" to "Stock",
                )
            )
        )
    }
}

private fun Route.deleteRoutes() {
    deleteRoute("medicine", MedicineRepository)
    deleteRoute("company", CompanyRepository)
    deleteRoute("pharmacy", PharmacyRepository)

    deleteManufacturerRoute()
    deleteStockRoute()
}

private fun Route.deleteRoute(pathRoot: String, repository: AbstractRepository<*>) {
    get("/$pathRoot/delete") {
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

private fun Route.deleteManufacturerRoute() {
    get("/manufacturer/delete") {
        val medicineId = call.queryParameters["medicine"]!!.toLongOrNull()
        if (medicineId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        val companyId = call.queryParameters["company"]!!.toLongOrNull()
        if (companyId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        var found = false
        transaction {
            found = MedicineManufacturerRepository.hasManufacturer(medicineId, companyId)
            if (found) MedicineManufacturerRepository.removeManufacturer(medicineId, companyId)
        }

        if (!found) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respondRedirect("/manufacturer/view")
        }
    }
}

private fun Route.deleteStockRoute() {
    get("/stock/delete") {
        val medicineId = call.queryParameters["medicine"]!!.toLongOrNull()
        if (medicineId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        val pharmacyId = call.queryParameters["pharmacy"]!!.toLongOrNull()
        if (pharmacyId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        var found = false
        transaction {
            found = StockRepository.find(medicineId, pharmacyId) != null
            if (found) StockRepository.delete(medicineId, pharmacyId)
        }

        if (!found) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respondRedirect("/stock/view")
        }
    }
}

private fun Route.addRoutes() {
    addRoute("medicine", "Medicine")
    addRoute("company", "Company")
    addRoute("pharmacy", "Pharmacy")
    addRoute("manufacturer", "Manufacturer")
    addRoute("stock", "Stock")
}

private fun Route.addRoute(type: String, name: String) {
    get("/$type/add") {
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
