package ru.trfx.catalog.manufacturer

import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.trfx.catalog.company.CompanyTable
import ru.trfx.catalog.medicine.MedicineTable

object MedicineManufacturerTable : CompositeIdTable("medicine_manufacturer") {
    val medicineId = reference("medicine_id", MedicineTable.id, onDelete = ReferenceOption.CASCADE)
    val companyId = reference("company_id", CompanyTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(medicineId, companyId)
}
