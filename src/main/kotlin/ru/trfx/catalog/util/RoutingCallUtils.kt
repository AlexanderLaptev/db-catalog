package ru.trfx.catalog.util

import io.ktor.server.routing.*
import ru.trfx.catalog.util.PageConstants.DEFAULT_PAGE_SIZE
import ru.trfx.catalog.util.PageConstants.MAX_PAGE_SIZE
import ru.trfx.catalog.util.PageConstants.MIN_PAGE_SIZE
import kotlin.math.max

val RoutingCall.page: Int
    get() {
        val value = this.queryParameters["page"] ?: return 1
        return try {
            val asInt = value.toInt()
            max(asInt, 1)
        } catch (e: NumberFormatException) {
            1
        }
    }

val RoutingCall.pageSize: Int
    get() {
        val value = this.queryParameters["size"] ?: return DEFAULT_PAGE_SIZE
        return try {
            val asInt = value.toInt()
            clamp(asInt, MIN_PAGE_SIZE, MAX_PAGE_SIZE)
        } catch (e: NumberFormatException) {
            DEFAULT_PAGE_SIZE
        }
    }
