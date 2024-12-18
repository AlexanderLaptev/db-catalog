package ru.trfx.catalog.stock

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.util.countPages
import ru.trfx.catalog.util.paginate

object StockRepository {
    private fun ResultRow.toModel(): Stock = Stock(
        this[StockTable.medicineId].value,
        this[StockTable.pharmacyId].value,
        this[StockTable.count],
        this[StockTable.price],
    )

    fun getAll(page: Int, pageSize: Int): PageResponse<Stock> {
        val lastRow = StockTable.selectAll().count()
        val lastPage = countPages(lastRow, pageSize)

        val data = StockTable
            .selectAll()
            .paginate(page, pageSize)
            .orderBy(StockTable.medicineId)
            .orderBy(StockTable.pharmacyId)
            .map { it.toModel() }

        return PageResponse(lastPage, lastRow, data)
    }

    fun find(medicineId: Long, pharmacyId: Long): Stock? {
        return StockTable
            .selectAll()
            .where {
                ((StockTable.medicineId eq medicineId)
                        and (StockTable.pharmacyId eq pharmacyId))
            }
            .map { it.toModel() }
            .firstOrNull()
    }

    fun create(stock: Stock): Boolean {
        stock.validate()
        return StockTable.insert {
            it[medicineId] = stock.medicineId
            it[pharmacyId] = stock.pharmacyId
            it[count] = stock.count
            it[price] = stock.price
        }.insertedCount > 0
    }

    fun update(stock: Stock) {
        stock.validate()
        StockTable.update({
            ((StockTable.medicineId eq stock.medicineId)
                    and (StockTable.pharmacyId eq stock.pharmacyId))
        }) {
            it[count] = stock.count
            it[price] = stock.price
        }
    }

    fun delete(medicineId: Long, pharmacyId: Long): Boolean {
        return StockTable.deleteWhere {
            ((StockTable.medicineId eq medicineId)
                    and (StockTable.pharmacyId eq pharmacyId))
        } > 0
    }

    fun deleteAll(): Int = StockTable.deleteAll()
}
