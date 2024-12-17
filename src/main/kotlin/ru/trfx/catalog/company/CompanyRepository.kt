package ru.trfx.catalog.company

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.trfx.catalog.repository.AbstractRepository
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.util.countPages
import ru.trfx.catalog.util.paginate

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
        val lastRow = CompanyTable.selectAll().count()
        val lastPage = countPages(lastRow, pageSize)
        val data = CompanyTable
            .selectAll()
            .paginate(page, pageSize)
            .where { CompanyTable.countryCode eq countryCode.uppercase() }
            .orderBy(nameColumn)
            .map { it.toModel() }

        return PageResponse(lastPage, lastRow, data)
    }
}
