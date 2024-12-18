package ru.trfx.catalog.company

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.trfx.catalog.manufacturer.MedicineManufacturerTable
import ru.trfx.catalog.repository.AbstractRepository
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.util.countPages
import ru.trfx.catalog.util.paginate

// TODO: handle bad country codes
object CompanyRepository : AbstractRepository<Company>(
    CompanyTable,
    CompanyTable.name
) {
    override fun UpdateBuilder<Int>.upsertInternal(entity: Company) {
        this[CompanyTable.name] = entity.name
        this[CompanyTable.countryCode] = entity.countryCode
    }

    override fun ResultRow.toModel(): Company =
        Company(this[CompanyTable.name], this[CompanyTable.countryCode], this[CompanyTable.id].value)

    fun findByCountry(countryCode: String, page: Int, pageSize: Int): PageResponse<Company> {
        val condition = CompanyTable.countryCode eq countryCode.uppercase()

        val lastRow = CompanyTable.selectAll().where(condition).count()
        val lastPage = countPages(lastRow, pageSize)

        val data = CompanyTable
            .selectAll()
            .paginate(page, pageSize)
            .where(condition)
            .orderBy(nameColumn)
            .map { it.toModel() }

        return PageResponse(lastPage, lastRow, data)
    }

    fun findByMedicineId(medicineId: Long, page: Int, pageSize: Int): PageResponse<Company> {
        val condition = MedicineManufacturerTable.medicineId eq medicineId
        val table = MedicineManufacturerTable innerJoin CompanyTable

        val lastRow = table.selectAll().where(condition).count()
        val lastPage = countPages(lastRow, pageSize)

        val data = (MedicineManufacturerTable innerJoin CompanyTable)
            .selectAll()
            .paginate(page, pageSize)
            .where(condition)
            .orderBy(CompanyTable.id)
            .map { it.toModel() }

        return PageResponse(lastPage, lastRow, data)
    }
}
