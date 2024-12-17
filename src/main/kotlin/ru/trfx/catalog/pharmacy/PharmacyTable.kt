package ru.trfx.catalog.pharmacy

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.or

object PharmacyTable : LongIdTable("pharmacy") {
    val name = varchar("name", 512).uniqueIndex()
    val websiteUrl = varchar("website", 2048).nullable()
    val latitude = double("latitude").check { (it greaterEq -90.0) or (it lessEq 90.0) }
    val longitude = double("longitude").check { (it greaterEq 0.0) or (it lessEq 180.0) }
}
