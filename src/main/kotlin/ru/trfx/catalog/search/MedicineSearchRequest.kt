package ru.trfx.catalog.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicineSearchRequest(
    val names: String? = null,
    val countries: String? = null,
    @SerialName("min_price") val minPrice: Double? = null,
    @SerialName("max_price") val maxPrice: Double? = null,
    @SerialName("in_stock") val onlyInStock: Boolean = false,
) {
    val isValid: Boolean = (names != null
            || countries != null
            || minPrice != null
            || maxPrice != null)
}
