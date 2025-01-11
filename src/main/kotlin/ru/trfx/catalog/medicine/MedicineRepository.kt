package ru.trfx.catalog.medicine

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.avg
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.min
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.trfx.catalog.manufacturer.MedicineManufacturerTable
import ru.trfx.catalog.repository.AbstractRepository
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.stock.StockTable
import ru.trfx.catalog.util.countPages
import ru.trfx.catalog.util.paginate

object MedicineRepository : AbstractRepository<Medicine>(
    MedicineTable,
    MedicineTable.name
) {
    override fun UpdateBuilder<Int>.upsertInternal(entity: Medicine) {
        this[MedicineTable.name] = entity.name
    }

    override fun ResultRow.toModel(): Medicine =
        Medicine(this[MedicineTable.name], this[MedicineTable.id].value)

    fun findByCompanyId(companyId: Long, page: Int, pageSize: Int): PageResponse<Medicine> {
        val expression = MedicineManufacturerTable.companyId eq companyId
        val table = MedicineManufacturerTable innerJoin MedicineTable

        val lastRow = table.selectAll().where(expression).count()
        val lastPage = countPages(lastRow, pageSize)

        val data = table
            .selectAll()
            .paginate(page, pageSize)
            .where(expression)
            .orderBy(MedicineTable.id)
            .map { it.toModel() }

        return PageResponse(lastPage, lastRow, data)
    }

    fun findByPharmacyId(pharmacyId: Long, page: Int, pageSize: Int): PageResponse<Medicine> {
        val expression = StockTable.pharmacyId eq pharmacyId
        val table = StockTable innerJoin MedicineTable

        val lastRow = table.selectAll().where(expression).count()
        val lastPage = countPages(lastRow, pageSize)

        val data = table
            .selectAll()
            .paginate(page, pageSize)
            .where(expression)
            .orderBy(MedicineTable.id)
            .map { it.toModel() }

        return PageResponse(lastPage, lastRow, data)
    }

    fun calcAveragePrice(): Double {
        return StockTable
            .select(StockTable.price.avg())
            .map {
                it[StockTable.price.avg()]
            }
            .first()?.toDouble() ?: Double.NaN
    }

    fun calcMinPrice(): Double {
        return StockTable
            .select(StockTable.price.min())
            .map {
                it[StockTable.price.min()]
            }
            .first() as Double
    }

    fun calcMaxPrice(): Double {
        return StockTable
            .select(StockTable.price.max())
            .map {
                it[StockTable.price.max()]
            }
            .first() as Double
    }

    fun count(): Long {
        return table.selectAll().count()
    }
}
