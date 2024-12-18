package ru.trfx.catalog.manufacturer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicineManufacturerRelation(
    @SerialName("medicine_id") val medicineId: Long,
    @SerialName("company_id") val companyId: Long,
)
