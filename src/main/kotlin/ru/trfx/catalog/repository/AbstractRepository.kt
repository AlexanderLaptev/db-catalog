package ru.trfx.catalog.repository

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upperCase
import ru.trfx.catalog.response.PageResponse
import ru.trfx.catalog.util.countPages
import ru.trfx.catalog.util.escapeSqlTemplates
import ru.trfx.catalog.util.paginate

abstract class AbstractRepository<T : IdEntity>(
    val table: LongIdTable,
    val nameColumn: Column<String>,
) {
    open fun getAll(page: Int, pageSize: Int): PageResponse<T> {
        val lastRow = table.selectAll().count()
        val lastPage = countPages(lastRow, pageSize)
        val data = table
            .selectAll()
            .paginate(page, pageSize)
            .orderBy(nameColumn)
            .map { it.toModel() }
        return PageResponse(lastPage, lastRow, data)
    }

    open fun findById(id: Long): T? {
        return table
            .selectAll()
            .where { table.id eq id }
            .map { it.toModel() }
            .firstOrNull()
    }

    open fun existsById(id: Long): Boolean {
        return table
            .selectAll()
            .where { table.id eq id }
            .count() > 0
    }

    open fun findByNameFuzzy(fuzzyName: String, page: Int, pageSize: Int): PageResponse<T> {
        val name = fuzzyName.escapeSqlTemplates()
        val expression = nameColumn.upperCase() like "%${name.uppercase()}%"
        val lastRow = table.selectAll().where(expression).count()
        val lastPage = countPages(lastRow, pageSize)

        val data = table
            .selectAll()
            .paginate(page, pageSize)
            .where(expression)
            .orderBy(nameColumn)
            .map { it.toModel() }

        return PageResponse(lastPage, lastRow, data)
    }

    open fun findByNameExact(exactName: String): T? {
        return table
            .selectAll()
            .where { nameColumn.upperCase() eq exactName.uppercase() }
            .map { it.toModel() }
            .firstOrNull()
    }

    open fun create(entity: T): Long {
        require(entity.id == null) { "Entity must not have an assigned ID" }
        val id = table.insertAndGetId { it.upsertInternal(entity) }
        return id.value
    }

    open fun update(entity: T) {
        require(entity.id != null) { "No entity ID specified" }
        table.update({ table.id eq entity.id }) { it.upsertInternal(entity) }
    }

    open fun deleteById(id: Long): Boolean {
        return table.deleteWhere { table.id eq id } > 0
    }

    open fun deleteAll() {
        table.deleteAll()
    }

    protected abstract fun UpdateBuilder<Int>.upsertInternal(entity: T)

    abstract fun ResultRow.toModel(): T
}
