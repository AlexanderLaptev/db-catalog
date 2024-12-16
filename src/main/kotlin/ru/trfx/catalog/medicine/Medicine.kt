package ru.trfx.catalog.medicine

import ru.trfx.catalog.repository.IdEntity

@kotlinx.serialization.Serializable
data class Medicine(
    val name: String,
    override val id: Long? = null,
) : IdEntity
