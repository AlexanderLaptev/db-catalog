package ru.trfx.catalog.company

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upperCase
import ru.trfx.catalog.util.escapeTemplates
import ru.trfx.catalog.util.getPageCount
import ru.trfx.catalog.util.paginate

object CompanyRepository {
    fun getAll(page: Int, limit: Int): List<Company> = transaction {
        CompanyTable
            .selectAll()
            .paginate(page, limit)
            .orderBy(CompanyTable.id)
            .map { it.toModel() }
    }

    fun getPageCount(pageSize: Int): Int = CompanyTable.getPageCount(pageSize)

    fun existsById(id: Long): Boolean = transaction {
        CompanyTable
            .select(CompanyTable.id)
            .where { CompanyTable.id eq id }
            .count() > 0
    }

    fun findById(id: Long): Company? = transaction {
        CompanyTable
            .selectAll()
            .where { CompanyTable.id eq id }
            .map { it.toModel() }
            .firstOrNull()
    }

    fun findByName(inName: String, page: Int, limit: Int): List<Company> = transaction {
        val name = inName.escapeTemplates()
        CompanyTable
            .selectAll()
            .paginate(page, limit)
            .where { CompanyTable.name.upperCase() like "%${name.uppercase()}%" }
            .orderBy(CompanyTable.name)
            .map { it.toModel() }
    }

    fun findByNameExact(name: String): Company? = transaction {
        CompanyTable
            .selectAll()
            .where { CompanyTable.name.upperCase() eq name.uppercase() }
            .map { it.toModel() }
            .firstOrNull()
    }

    // TODO: look into skipping IDs after a constraint violation
    fun save(company: Company): Company = transaction {
        require(company.id == null) { "Object already has an ID" }

        val id = CompanyTable
            .insertAndGetId {
                it[name] = company.name
                it[country] = company.countryCode
            }
        company.copy(id = id.value)
    }

    // TODO: handle missing IDs
    fun update(company: Company): Company = transaction {
        require(company.id != null) { "No ID specified" }
        CompanyTable
            .update({ CompanyTable.id eq company.id }) {
                it[name] = company.name
            }
        company
    }

    fun deleteById(id: Long): Boolean = transaction {
        val count = CompanyTable
            .deleteWhere { CompanyTable.id eq id }
        count > 0
    }

    fun deleteAll(): Unit = transaction {
        CompanyTable.deleteAll()
    }

    private fun ResultRow.toModel(): Company = Company(
        this[CompanyTable.name],
        this[CompanyTable.country],
        this[CompanyTable.id].value,
    )
}
