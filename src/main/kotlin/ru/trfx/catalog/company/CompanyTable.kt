package ru.trfx.catalog.company

import org.jetbrains.exposed.dao.id.LongIdTable

object CompanyTable : LongIdTable("company") {
    val name = varchar("name", 512).uniqueIndex()
    val country = char("country", 2)
}
