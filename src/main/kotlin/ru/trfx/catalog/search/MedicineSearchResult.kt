package ru.trfx.catalog.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicineSearchResult(
    @SerialName("medicine_id") val medicineId: Long,
    @SerialName("medicine_name") val medicineName: String,

    @SerialName("pharmacy_id") val pharmacyId: Long,
    @SerialName("pharmacy_name") val pharmacyName: String,
    @SerialName("pharmacy_geo") val pharmacyGeo: String,

    @SerialName("company_id") val companyId: Long,
    @SerialName("company_name") val companyName: String,

    val country: String,
    val price: Double,
    val count: Int,
)
