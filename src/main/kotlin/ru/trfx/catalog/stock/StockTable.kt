package ru.trfx.catalog.stock

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.trfx.catalog.medicine.MedicineTable
import ru.trfx.catalog.pharmacy.PharmacyTable

object StockTable : CompositeIdTable("stock") {
    val medicineId = reference("medicine_id", MedicineTable.id, onDelete = ReferenceOption.CASCADE)
    val pharmacyId = reference("pharmacy_id", PharmacyTable.id, onDelete = ReferenceOption.CASCADE)
    val count = integer("count").check { it greaterEq 0 }
    val price = double("price").check { it greater 0.0 }

    override val primaryKey = PrimaryKey(medicineId, pharmacyId)
}
