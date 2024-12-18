package ru.trfx.catalog.pharmacy

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.trfx.catalog.medicine.MedicineTable
import ru.trfx.catalog.repository.AbstractRepository
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.stock.StockTable
import ru.trfx.catalog.util.countPages
import ru.trfx.catalog.util.paginate

object PharmacyRepository : AbstractRepository<Pharmacy>(
    PharmacyTable,
    PharmacyTable.name
) {
    override fun UpdateBuilder<Int>.upsertInternal(entity: Pharmacy) {
        this[PharmacyTable.name] = entity.name
        if (entity.websiteUrl != null) {
            this[PharmacyTable.websiteUrl] = entity.websiteUrl
        }
        this[PharmacyTable.latitude] = entity.latitude
        this[PharmacyTable.longitude] = entity.longitude
    }

    override fun ResultRow.toModel(): Pharmacy = Pharmacy(
        this[PharmacyTable.name],
        this[PharmacyTable.latitude],
        this[PharmacyTable.longitude],
        this[PharmacyTable.websiteUrl],
        this[PharmacyTable.id].value
    )

    fun findByMedicineId(medicineId: Long, page: Int, pageSize: Int): PageResponse<Pharmacy> {
        val expression = StockTable.medicineId eq medicineId
        val table = StockTable innerJoin MedicineTable

        val lastRow = table.selectAll().where(expression).count()
        val lastPage = countPages(lastRow, pageSize)

        val data = table
            .selectAll()
            .paginate(page, pageSize)
            .where(expression)
            .orderBy(PharmacyTable.id)
            .map { it.toModel() }

        return PageResponse(lastPage, lastRow, data)
    }
}
