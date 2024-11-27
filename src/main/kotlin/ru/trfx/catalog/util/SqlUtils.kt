package ru.trfx.catalog.util

import org.jetbrains.exposed.sql.Query

const val MAX_PAGE_SIZE = 200

fun Query.paginate(page: Int, limit: Int): Query {
//    val actualLimit = if (limit > MAX_PAGE_SIZE) MAX_PAGE_SIZE else limit
    val actualLimit = limit
    this
        .limit(actualLimit)
        .offset((actualLimit * page).toLong())
    return this
}

fun String.escapeTemplates(): String =
    this.replace("_", "\\_").replace("%", "\\%")
