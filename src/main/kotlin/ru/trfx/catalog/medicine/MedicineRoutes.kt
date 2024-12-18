package ru.trfx.catalog.medicine

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.response.ErrorResponse
import ru.trfx.catalog.route.AbstractRoutes
import ru.trfx.catalog.util.page
import ru.trfx.catalog.util.pageSize

object MedicineRoutes : AbstractRoutes<Medicine>(
    "medicine",
    MedicineRepository,
    Medicine::class,
    Medicine.serializer(),
) {
    override fun customRoutes(route: Route) {
        with(route) {
            findByCompanyIdRoute()
        }
    }

    private fun Route.findByCompanyIdRoute() {
        get("/byCompany/{id}") {
            val id = call.pathParameters["id"]!!.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
                return@get
            }

            val response = transaction { MedicineRepository.findByCompanyId(id, call.page, call.pageSize) }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}
