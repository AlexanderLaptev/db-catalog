package ru.trfx.catalog.pharmacy

import kotlinx.serialization.SerialName
import ru.trfx.catalog.repository.IdEntity

@kotlinx.serialization.Serializable
data class Pharmacy(
    override val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("website") val websiteUrl: String? = null,
    override val id: Long? = null,
) : IdEntity {
    override fun validate() {
        require(latitude in -90.0..90.0) { "Latitude must be between -90.0 and 90.0 (inclusive)" }
        require(longitude in 0.0..180.0) { "Longitude must be between 0.0 and 180.0 (inclusive)" }
    }
}
