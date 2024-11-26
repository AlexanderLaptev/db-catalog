package ru.trfx.catalog.medicine

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
import ru.trfx.catalog.util.paginate

object MedicineRepository {
    fun getAll(page: Int, limit: Int): List<Medicine> = transaction {
        MedicineTable
            .selectAll()
            .paginate(page, limit)
            .orderBy(MedicineTable.id)
            .map { it.toModel() }
    }

    fun existsById(id: Long): Boolean = transaction {
        MedicineTable
            .select(MedicineTable.id)
            .where { MedicineTable.id eq id }
            .count() > 0
    }

    fun findById(id: Long): Medicine? = transaction {
        MedicineTable
            .selectAll()
            .where { MedicineTable.id eq id }
            .map { it.toModel() }
            .firstOrNull()
    }

    fun findByName(inName: String, page: Int, limit: Int): List<Medicine> = transaction {
        val name = inName.escapeTemplates()
        MedicineTable
            .selectAll()
            .paginate(page, limit)
            .where { MedicineTable.name.upperCase() like "%${name.uppercase()}%" }
            .orderBy(MedicineTable.name)
            .map { it.toModel() }
    }

    fun findByNameExact(name: String): Medicine? = transaction {
        MedicineTable
            .selectAll()
            .where { MedicineTable.name.upperCase() eq name.uppercase() }
            .map { it.toModel() }
            .firstOrNull()
    }

    // TODO: look into skipping IDs after a constraint violation
    fun save(medicine: Medicine): Medicine = transaction {
        require(medicine.id == null) { "Object already has an ID" }

        val id = MedicineTable
            .insertAndGetId {
                it[name] = medicine.name
            }
        medicine.copy(id = id.value)
    }

    // TODO: handle missing IDs
    fun update(medicine: Medicine): Medicine = transaction {
        require(medicine.id != null) { "No ID specified" }
        MedicineTable
            .update({ MedicineTable.id eq medicine.id }) {
                it[name] = medicine.name
            }
        medicine
    }

    fun deleteById(id: Long): Boolean = transaction {
        val count = MedicineTable
            .deleteWhere { MedicineTable.id eq id }
        count > 0
    }

    fun deleteAll(): Unit = transaction {
        MedicineTable.deleteAll()
    }

    private fun ResultRow.toModel(): Medicine = Medicine(this[MedicineTable.name], this[MedicineTable.id].value)
}
