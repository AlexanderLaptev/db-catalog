package ru.trfx.catalog.util

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.ceil
import kotlin.math.max

fun Query.paginate(inPage: Int, inSize: Int): Query {
    val page = max(0, inPage - 1)
    val size = clamp(inSize, PageConstants.MIN_PAGE_SIZE, PageConstants.MAX_PAGE_SIZE)
    this
        .limit(size)
        .offset((page * size).toLong())
    return this
}

fun Table.getPageCount(pageSize: Int) = transaction {
    val count = this@getPageCount
        .selectAll()
        .count()
    ceil(count.toFloat() / pageSize.toFloat()).toInt()
}

fun String.escapeTemplates(): String =
    this.replace("_", "\\_").replace("%", "\\%")
