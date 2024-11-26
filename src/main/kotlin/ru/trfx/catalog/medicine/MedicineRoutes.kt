package ru.trfx.catalog.medicine

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.trfx.catalog.response.ErrorResponse

fun Application.medicineRoutes() {
    routing {
        route("/api/medicine") {
            getAllMedicinesRoute()
            findMedicineByIdRoute()
            findMedicineByNameRoute()
            createMedicineRoute()
            updateMedicineRoute()
            deleteMedicineRoute()
        }
    }
}

private fun Route.getAllMedicinesRoute() {
    get("/all") {
        val page = call.queryParameters["page"]?.toInt() ?: 0
        val limit = call.queryParameters["limit"]?.toInt() ?: 20
        val result = MedicineRepository.getAll(page, limit)
        call.respond(HttpStatusCode.OK, result)
    }
}

private fun Route.findMedicineByIdRoute() {
    get("/{id}") {
        val id = call.pathParameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        val result = MedicineRepository.findById(id)
        if (result == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(HttpStatusCode.OK, result)
        }
    }
}

private fun Route.findMedicineByNameRoute() {
    get("/byName/{name}") {
        val name = call.pathParameters["name"]
        if (name == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Empty name"))
            return@get
        }

        val exact = call.queryParameters["exact"] == "1"
        if (exact) {
            val result = MedicineRepository.findByNameExact(name)
            if (result == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, result)
            }
        } else {
            val result = MedicineRepository.findByName(name)
            call.respond(HttpStatusCode.OK, result)
        }

    }
}

private fun Route.createMedicineRoute() {
    post {
        try {
            val medicine = call.receive<Medicine>()
            if (medicine.id != null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Manual ID assignment not allowed"))
                return@post
            }

            val result = MedicineRepository.save(medicine)
            call.respond(HttpStatusCode.Created, result)
        } catch (e: BadRequestException) {
            call.application.log.trace("Handled exception: ", e)
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Bad object"))
            return@post
        } catch (e: ExposedSQLException) {
            if (e.cause is JdbcSQLIntegrityConstraintViolationException) {
                call.application.log.trace("Handled exception: ", e)
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Object already exists"))
                return@post
            }
        }
    }
}

private fun Route.updateMedicineRoute() {
    put("/{id}") {
        val id = call.pathParameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@put
        }

        try {
            val medicine = call.receive<Medicine>()
            if (medicine.id != null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Body must not contain ID"))
                return@put
            }

            val result = MedicineRepository.update(medicine.copy(id = id))
            call.respond(HttpStatusCode.NoContent, result)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Bad object"))
            return@put
        }
    }
}

private fun Route.deleteMedicineRoute() {
    delete("/{id}") {
        val id = call.pathParameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@delete
        }

        MedicineRepository.deleteById(id)
        call.respond(HttpStatusCode.NoContent)
    }
}
