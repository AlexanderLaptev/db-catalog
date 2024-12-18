package ru.trfx.catalog.stock

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.response.ErrorResponse
import ru.trfx.catalog.util.page
import ru.trfx.catalog.util.pageSize

fun Application.stockRoutes() {
    routing {
        route("/api/stock") {
            getAllRoute()
            findRoute()
            addRoute()
            updateRoute()
            deleteRoute()
        }
    }
}

private fun Route.getAllRoute() {
    get("/all") {
        val response = transaction { StockRepository.getAll(call.page, call.pageSize) }
        call.respond(HttpStatusCode.OK, response)
    }
}

private fun Route.findRoute() {
    get {
        val medicineId = call.queryParameters["medicine"]?.toLongOrNull()
        if (medicineId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Missing or malformed medicine ID"))
            return@get
        }

        val pharmacyId = call.queryParameters["pharmacy"]?.toLongOrNull()
        if (pharmacyId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Missing or malformed pharmacy ID"))
            return@get
        }

        val stock = transaction { StockRepository.find(medicineId, pharmacyId) }
        if (stock == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(HttpStatusCode.OK, stock)
        }
    }
}

private fun Route.addRoute() {
    post {
        val stock = try {
            call.receive<Stock>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid object body format"))
            return@post
        }

        try {
            stock.validate()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Unknown error"))
            return@post
        }

        transaction { StockRepository.create(stock) }
        call.respond(HttpStatusCode.NoContent)
    }
}

private fun Route.updateRoute() {
    put {
        val stock = try {
            call.receive<Stock>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid object body format"))
            return@put
        }

        try {
            stock.validate()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Unknown error"))
            return@put
        }

        transaction { StockRepository.update(stock) }
        call.respond(HttpStatusCode.NoContent)
    }
}

private fun Route.deleteRoute() {
    delete {
        val medicineId = call.queryParameters["medicine"]?.toLongOrNull()
        if (medicineId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Missing or malformed medicine ID"))
            return@delete
        }

        val pharmacyId = call.queryParameters["pharmacy"]?.toLongOrNull()
        if (pharmacyId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Missing or malformed pharmacy ID"))
            return@delete
        }

        val success = transaction { StockRepository.delete(medicineId, pharmacyId) }
        call.respond(if (success) HttpStatusCode.NoContent else HttpStatusCode.NotFound)
    }
}
