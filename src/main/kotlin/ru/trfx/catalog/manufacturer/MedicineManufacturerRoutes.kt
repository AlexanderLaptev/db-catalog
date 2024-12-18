package ru.trfx.catalog.manufacturer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.response.ErrorResponse
import java.sql.SQLException

fun Application.medicineManufacturerRoutes() {
    routing {
        route("/api/manufacturer") {
            addRoute()
            deleteRoute()
        }
    }
}

private fun Route.addRoute() {
    post {
        val medicineId = call.queryParameters["medicine"]!!.toLongOrNull()
        if (medicineId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@post
        }

        val companyId = call.queryParameters["company"]!!.toLongOrNull()
        if (companyId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@post
        }

        try {
            transaction { MedicineManufacturerRepository.addManufacturer(medicineId, companyId) }
        } catch (e: SQLException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Illegal object data"))
            return@post
        }

        call.respond(HttpStatusCode.NoContent)
    }
}

private fun Route.deleteRoute() {
    delete {
        val medicineId = call.queryParameters["medicine"]!!.toLongOrNull()
        if (medicineId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@delete
        }

        val companyId = call.queryParameters["company"]!!.toLongOrNull()
        if (companyId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@delete
        }

        try {
            transaction { MedicineManufacturerRepository.removeManufacturer(medicineId, companyId) }
        } catch (e: SQLException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Illegal object data"))
            return@delete
        }

        call.respond(HttpStatusCode.NoContent)
    }
}
