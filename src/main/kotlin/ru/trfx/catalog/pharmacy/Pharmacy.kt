package ru.trfx.catalog.pharmacy

import ru.trfx.catalog.repository.IdEntity

@kotlinx.serialization.Serializable
data class Pharmacy(
    val name: String,
    val websiteUrl: String? = null,
    override val id: Long? = null,
) : IdEntity
