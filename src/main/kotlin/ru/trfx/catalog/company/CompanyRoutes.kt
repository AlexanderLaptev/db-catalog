package ru.trfx.catalog.company

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.trfx.catalog.response.ErrorResponse
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.util.page
import ru.trfx.catalog.util.pageSize

fun Application.companyRoutes() {
    routing {
        route("/api/company") {
            getAllCompaniesRoute()
            findCompanyByIdRoute()
            findCompanyByNameRoute()
            createCompanyRoute()
            updateCompanyRoute()
            deleteCompanyRoute()
        }
    }
}

private fun Route.getAllCompaniesRoute() {
    get("/all") {
        val pageSize = call.pageSize
        val pageCount = CompanyRepository.getPageCount(pageSize)
        val data = CompanyRepository.getAll(call.page, pageSize)
        call.respond(HttpStatusCode.OK, PageResponse(pageCount, data))
    }
}

private fun Route.findCompanyByIdRoute() {
    get("/{id}") {
        val id = call.pathParameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@get
        }

        val result = CompanyRepository.findById(id)
        if (result == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(HttpStatusCode.OK, result)
        }
    }
}

private fun Route.findCompanyByNameRoute() {
    get("/byName/{name}") {
        val name = call.pathParameters["name"]
        if (name == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Empty name"))
            return@get
        }

        val exact = call.queryParameters["exact"] == "1"
        if (exact) {
            val result = CompanyRepository.findByNameExact(name)
            if (result == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, result)
            }
        } else {
            val result = CompanyRepository.findByName(name, call.page, call.pageSize)
            call.respond(HttpStatusCode.OK, result)
        }

    }
}

private fun Route.createCompanyRoute() {
    post {
        try {
            val company = call.receive<Company>()
            if (company.id != null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Manual ID assignment not allowed"))
                return@post
            }

            val result = CompanyRepository.save(company)
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

private fun Route.updateCompanyRoute() {
    put("/{id}") {
        val id = call.pathParameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@put
        }

        try {
            val company = call.receive<Company>()
            if (company.id != null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Body must not contain ID"))
                return@put
            }

            val result = CompanyRepository.update(company.copy(id = id))
            call.respond(HttpStatusCode.NoContent, result)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Bad object"))
            return@put
        }
    }
}

private fun Route.deleteCompanyRoute() {
    delete("/{id}") {
        val id = call.pathParameters["id"]?.toLongOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
            return@delete
        }

        CompanyRepository.deleteById(id)
        call.respond(HttpStatusCode.NoContent)
    }
}
