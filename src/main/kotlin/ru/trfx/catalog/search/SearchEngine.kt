package ru.trfx.catalog.search

import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upperCase
import ru.trfx.catalog.company.CompanyTable
import ru.trfx.catalog.manufacturer.MedicineManufacturerTable
import ru.trfx.catalog.medicine.MedicineTable
import ru.trfx.catalog.pharmacy.PharmacyTable
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.stock.StockTable
import ru.trfx.catalog.util.countPages
import ru.trfx.catalog.util.paginate

object SearchEngine {
    fun searchMedicines(
        request: MedicineSearchRequest,
        page: Int,
        pageSize: Int,
    ): PageResponse<MedicineSearchResult> {
        val table = StockTable
            .join(MedicineTable, JoinType.INNER, StockTable.medicineId, MedicineTable.id)
            .join(PharmacyTable, JoinType.INNER, StockTable.pharmacyId, PharmacyTable.id)
            .join(
                MedicineManufacturerTable,
                JoinType.INNER,
                StockTable.medicineId,
                MedicineManufacturerTable.medicineId
            )
            .join(CompanyTable, JoinType.INNER, MedicineManufacturerTable.companyId, CompanyTable.id)

        var query = table.selectAll()

        if (request.names != null) {
            val names = request.names.split("|").map { it.uppercase() }
            query = query.andWhere { MedicineTable.name.upperCase() inList names }
        }
        if (request.countries != null) {
            val countries = request.countries.split("|").map { it.uppercase() }
            query = query.andWhere { CompanyTable.countryCode.upperCase() inList countries }
        }
        if (request.minPrice != null) {
            query = query.andWhere { StockTable.price greaterEq request.minPrice }
        }
        if (request.maxPrice != null) {
            query = query.andWhere { StockTable.price lessEq request.maxPrice }
        }
        if (request.onlyInStock) {
            query = query.andWhere { StockTable.count greater 0 }
        }

        val lastRow = query.copy().count()
        val lastPage = countPages(lastRow, pageSize)

        val data = query
            .paginate(page, pageSize)
            .orderBy(MedicineTable.name)
            .orderBy(CompanyTable.name)
            .orderBy(StockTable.price)
            .orderBy(StockTable.count)
            .map { it.toResult() }

        return PageResponse(lastPage, lastRow, data)
    }

    private fun ResultRow.toResult() = MedicineSearchResult(
        this[MedicineTable.id].value,
        this[MedicineTable.name],

        this[PharmacyTable.id].value,
        this[PharmacyTable.name],
        "${this[PharmacyTable.latitude]} ${this[PharmacyTable.longitude]}",

        this[CompanyTable.id].value,
        this[CompanyTable.name],

        this[CompanyTable.countryCode],
        this[StockTable.price],
        this[StockTable.count],
    )
}
