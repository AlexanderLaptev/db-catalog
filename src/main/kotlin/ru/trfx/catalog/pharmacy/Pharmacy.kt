package ru.trfx.catalog.pharmacy

import ru.trfx.catalog.repository.IdEntity

@kotlinx.serialization.Serializable
data class Pharmacy(
    override val name: String,
    val websiteUrl: String? = null,
    override val id: Long? = null,
) : IdEntity
