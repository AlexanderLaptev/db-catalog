package ru.trfx.catalog.util

import io.ktor.server.routing.*

const val DEFAULT_LIMIT = Int.MAX_VALUE

val RoutingCall.requestedPage get() = this.queryParameters["page"]?.toIntOrNull() ?: 0
val RoutingCall.requestedLimit get() = this.queryParameters["limit"]?.toIntOrNull() ?: DEFAULT_LIMIT
