package ru.trfx.catalog.company

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.trfx.catalog.repository.AbstractRepository

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
}
