package ru.trfx.catalog.pharmacy

import org.jetbrains.exposed.dao.id.LongIdTable

object PharmacyTable : LongIdTable("pharmacy") {
    val name = varchar("name", 512).uniqueIndex()
    val websiteUrl = varchar("website", 2048)
}
