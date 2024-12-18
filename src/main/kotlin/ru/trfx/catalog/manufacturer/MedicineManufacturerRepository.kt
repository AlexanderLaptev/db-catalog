package ru.trfx.catalog.manufacturer

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.util.countPages
import ru.trfx.catalog.util.paginate

object MedicineManufacturerRepository {
    fun getAll(page: Int, pageSize: Int): PageResponse<MedicineManufacturerRelation> {
        val lastRow = MedicineManufacturerTable.selectAll().count()
        val lastPage = countPages(lastRow, pageSize)

        val data = MedicineManufacturerTable
            .selectAll()
            .paginate(page, pageSize)
            .orderBy(MedicineManufacturerTable.companyId)
            .orderBy(MedicineManufacturerTable.medicineId)
            .map {
                MedicineManufacturerRelation(
                    it[MedicineManufacturerTable.medicineId].value,
                    it[MedicineManufacturerTable.companyId].value
                )
            }

        return PageResponse(lastPage, lastRow, data)
    }

    fun addManufacturer(medicineId: Long, companyId: Long): Int {
        return MedicineManufacturerTable
            .insert {
                it[MedicineManufacturerTable.medicineId] = medicineId
                it[MedicineManufacturerTable.companyId] = companyId
            }.insertedCount
    }

    fun removeManufacturer(medicineId: Long, companyId: Long): Boolean {
        return MedicineManufacturerTable
            .deleteWhere {
                ((MedicineManufacturerTable.medicineId eq medicineId)
                        and (MedicineManufacturerTable.companyId eq companyId))
            } > 0
    }

    fun hasManufacturer(medicineId: Long, companyId: Long): Boolean {
        return MedicineManufacturerTable
            .selectAll()
            .where {
                ((MedicineManufacturerTable.medicineId eq medicineId)
                        and (MedicineManufacturerTable.companyId eq companyId))
            }
            .count() > 0
    }

    fun deleteAll() {
        MedicineManufacturerTable.deleteAll()
    }
}
