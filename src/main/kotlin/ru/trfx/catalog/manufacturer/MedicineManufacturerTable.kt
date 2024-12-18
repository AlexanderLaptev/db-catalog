package ru.trfx.catalog.manufacturer

import org.jetbrains.exposed.dao.id.CompositeIdTable
import ru.trfx.catalog.company.CompanyTable
import ru.trfx.catalog.medicine.MedicineTable

object MedicineManufacturerTable : CompositeIdTable("medicine_manufacturer") {
    val medicineId = reference("medicine_id", MedicineTable.id)
    val companyId = reference("company_id", CompanyTable.id)

    override val primaryKey = PrimaryKey(medicineId, companyId)
}
