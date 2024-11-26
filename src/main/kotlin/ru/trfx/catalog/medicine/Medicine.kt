package ru.trfx.catalog.medicine

@kotlinx.serialization.Serializable
data class Medicine(
    val name: String,
    val id: Long? = null,
)
