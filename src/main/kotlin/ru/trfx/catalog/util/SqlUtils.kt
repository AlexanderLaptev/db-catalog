package ru.trfx.catalog.util

import org.jetbrains.exposed.sql.Query
import kotlin.math.max

fun Query.paginate(inPage: Int, inSize: Int): Query {
    val page = max(0, inPage - 1)
    val size = clamp(inSize, PageConstants.MIN_PAGE_SIZE, PageConstants.MAX_PAGE_SIZE)
    this
        .limit(size)
        .offset((page * size).toLong())
    return this
}

fun String.escapeTemplates(): String =
    this.replace("_", "\\_").replace("%", "\\%")
