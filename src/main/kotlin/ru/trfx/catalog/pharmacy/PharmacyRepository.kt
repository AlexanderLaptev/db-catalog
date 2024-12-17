package ru.trfx.catalog.pharmacy

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.trfx.catalog.repository.AbstractRepository

object PharmacyRepository : AbstractRepository<Pharmacy>(
    PharmacyTable,
    PharmacyTable.name
) {
    override fun UpdateBuilder<Int>.upsertInternal(entity: Pharmacy) {
        this[PharmacyTable.name] = entity.name
        if (entity.websiteUrl != null) {
            this[PharmacyTable.websiteUrl] = entity.websiteUrl
        }
        this[PharmacyTable.latitude] = entity.latitude
        this[PharmacyTable.longitude] = entity.longitude
    }

    override fun ResultRow.toModel(): Pharmacy = Pharmacy(
        this[PharmacyTable.name],
        this[PharmacyTable.latitude],
        this[PharmacyTable.longitude],
        this[PharmacyTable.websiteUrl],
        this[PharmacyTable.id].value
    )
}
