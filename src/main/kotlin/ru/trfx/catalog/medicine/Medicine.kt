package ru.trfx.catalog.medicine

import kotlinx.serialization.Serializable
import ru.trfx.catalog.repository.IdEntity

@Serializable
data class Medicine(
    override val name: String,
    override val id: Long? = null,
) : IdEntity
