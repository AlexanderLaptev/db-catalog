package ru.trfx.catalog.medicine

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.trfx.catalog.repository.AbstractRepository

object MedicineRepository : AbstractRepository<Medicine>(
    MedicineTable,
    MedicineTable.name
) {
    override fun UpdateBuilder<Int>.upsertInternal(entity: Medicine) {
        this[MedicineTable.name] = entity.name
    }

    override fun ResultRow.toModel(): Medicine =
        Medicine(this[MedicineTable.name], this[MedicineTable.id].value)
}
