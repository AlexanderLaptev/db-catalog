package ru.trfx.catalog.search

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
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

        }

        route("/api/search") {
            get("/results") {
                val request = call.receive<MedicineSearchRequest>()
                val results = transaction { SearchEngine.searchMedicines(request, call.page, call.pageSize) }
                call.respond(HttpStatusCode.OK, results)
            }
        }
    }
}
