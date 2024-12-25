package ru.trfx.catalog.search

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.util.page
import ru.trfx.catalog.util.pageSize

fun Application.medicineSearchRoutes() {
    routing {
        route("/search") {
            get {
                call.respond(ThymeleafContent("search", mapOf()))
            }

            get("/results") {
                call.respond(ThymeleafContent("results", mapOf()))
            }
        }

        route("/api/search") {
            get {
                val request = MedicineSearchRequest(
                    call.queryParameters["names"]?.ifEmpty { null },
                    call.queryParameters["countries"]?.ifEmpty { null },
                    call.queryParameters["min-price"]?.toDoubleOrNull(),
                    call.queryParameters["max-price"]?.toDoubleOrNull(),
                    call.queryParameters["in-stock"]?.lowercase() == "on",
                )
                val results = transaction { SearchEngine.searchMedicines(request, call.page, call.pageSize) }
                call.respond(HttpStatusCode.OK, results)
            }
        }
    }
}
