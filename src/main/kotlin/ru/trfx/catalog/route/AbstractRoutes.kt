package ru.trfx.catalog.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
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
            }
        }
    }

    protected open fun Route.getAllRoute() {
        get("/all") {
            val pageResponse = repository.getAll(call.page, call.pageSize)
            val json = Json.encodeToJsonElement(PageResponse.serializer(serializer), pageResponse)
            call.respond(HttpStatusCode.OK, json)
        }
    }

    protected open fun Route.findByIdRoute() {
        get("/byId/{id}") {
            val id = call.pathParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val entity = repository.findById(id)
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
            val name = call.pathParameters["name"]
            if (name == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val entity = repository.findByNameExact(name)
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
            if (entity.id != null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Body must not contain ID"))
                return@post
            }

            repository.create(entity)
            call.respond(HttpStatusCode.NoContent)
        }
    }

    protected open fun Route.updateRoute() {
        put {
            val updateEntity = call.receive(entityClass)
            if (updateEntity.id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("No ID specified"))
                return@put
            }

            val exists = repository.existsById(updateEntity.id!!)
            if (!exists) {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }

            repository.update(updateEntity)
            call.respond(HttpStatusCode.NoContent)
        }
    }

    protected open fun Route.deleteRoute() {
        delete("/{id}") {
            val id = call.pathParameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            repository.deleteById(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}