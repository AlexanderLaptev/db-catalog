package ru.trfx.catalog.stock

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    @SerialName("medicine_id") val medicineId: Long,
    @SerialName("pharmacy_id") val pharmacyId: Long,
    val count: Int,
    val price: Double,
) {
    fun validate() {
        require(count >= 0) { "Count must be non-negative" }
        require(price > 0) { "Price must be > 0" }
    }
}
