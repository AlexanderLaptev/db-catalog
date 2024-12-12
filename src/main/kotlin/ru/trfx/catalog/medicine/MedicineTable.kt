package ru.trfx.catalog.medicine

import org.jetbrains.exposed.dao.id.LongIdTable

object MedicineTable : LongIdTable("medicine") {
    val name = varchar("name", 512).uniqueIndex()
}
