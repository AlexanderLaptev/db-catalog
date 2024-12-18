package ru.trfx.catalog.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.repository.AbstractRepository
import ru.trfx.catalog.repository.IdEntity
import ru.trfx.catalog.response.ErrorResponse
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.util.page
import ru.trfx.catalog.util.pageSize
import kotlin.reflect.KClass

abstract class AbstractRoutes<T : IdEntity>(
    val apiRoot: String,
    val repository: AbstractRepository<T>,
    val entityClass: KClass<T>,
    val serializer: KSerializer<T>,
) {
    open fun addRoutes(app: Application) {
        app.routing {
            route("/api/$apiRoot") {
                getAllRoute()
                findByIdRoute()
                findByNameRoute()
                insertRoute()
                updateRoute()
                deleteRoute()

                customRoutes(this)
            }
        }
    }

    protected open fun customRoutes(route: Route) = Unit

    protected open fun Route.getAllRoute() {
        get("/all") {
            val pageResponse = transaction { repository.getAll(call.page, call.pageSize) }
            val json = Json.encodeToJsonElement(PageResponse.serializer(serializer), pageResponse)
            call.respond(HttpStatusCode.OK, json)
        }
    }

    protected open fun Route.findByIdRoute() {
        get("/byId/{id}") {
            val id = call.pathParameters["id"]!!.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
                return@get
            }

            val entity = transaction { repository.findById(id) }
            if (entity != null) {
                val json = Json.encodeToJsonElement(serializer, entity)
                call.respond(HttpStatusCode.OK, json)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }

    protected open fun Route.findByNameRoute() {
        get("/byName/{name}") {
            val name = call.pathParameters["name"]!!
            val entity = transaction { repository.findByNameExact(name) }
            if (entity != null) {
                val json = Json.encodeToJsonElement(serializer, entity)
                call.respond(HttpStatusCode.OK, json)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }

    protected open fun Route.insertRoute() {
        post {
            val entity = call.receive(entityClass)
            try {
                validateEntityOrThrow(entity)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Unknown error"))
                return@post
            }

            transaction { repository.create(entity) }
            call.respond(HttpStatusCode.NoContent)
        }
    }

    protected open fun Route.updateRoute() {
        put {
            val updateEntity = call.receive(entityClass)
            try {
                validateEntityOrThrow(updateEntity)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Unknown error"))
                return@put
            }

            var exists = false
            transaction {
                exists = repository.existsById(updateEntity.id!!)
                if (exists) repository.update(updateEntity)
            }
            call.respond(if (exists) HttpStatusCode.NoContent else HttpStatusCode.NotFound)
        }
    }

    protected open fun validateEntityOrThrow(entity: T) {
        require(entity.id != null) { "No ID specified" }
    }

    protected open fun Route.deleteRoute() {
        delete("/{id}") {
            val id = call.pathParameters["id"]!!.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Malformed ID"))
                return@delete
            }

            transaction { repository.deleteById(id) }
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
