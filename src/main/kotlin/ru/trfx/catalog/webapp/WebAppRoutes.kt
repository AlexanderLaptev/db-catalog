package ru.trfx.catalog.webapp

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.webAppRoutes() {
    routing {
        staticResources("/scripts", "scripts")
        medicineRoute()
    }
}

private fun Route.medicineRoute() {
    route("/medicines") {
        staticResources("", "static", index = "table.html")
    }
}
