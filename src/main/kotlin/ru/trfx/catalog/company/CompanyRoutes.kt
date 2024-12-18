package ru.trfx.catalog.company

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.response.ErrorResponse
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.route.AbstractRoutes
import ru.trfx.catalog.util.page
import ru.trfx.catalog.util.pageSize

object CompanyRoutes : AbstractRoutes<Company>(
    "company",
    CompanyRepository,
    Company::class,
    Company.serializer(),
) {
    override fun customRoutes(route: Route) {
        with(route) {
            findByCountryRoute()
        }
    }

    private fun Route.findByCountryRoute() {
        get("/byCountry/{country}") {
            val country = call.pathParameters["country"]!!.uppercase()
            if (country !in Company.COUNTRY_CODES) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Country must be a valid 2-letter ISO country"))
                return@get
            }

            val response = transaction { CompanyRepository.findByCountry(country, call.page, call.pageSize) }
            val json = Json.encodeToJsonElement(PageResponse.serializer(serializer), response)
            call.respond(HttpStatusCode.OK, json)
        }
    }

    override fun validateEntityOrThrow(entity: Company) {
        super.validateEntityOrThrow(entity)
        require(entity.countryCode.uppercase() in Company.COUNTRY_CODES) { "Country must be a valid 2-letter ISO country" }
    }
}
