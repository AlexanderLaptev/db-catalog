package ru.trfx.catalog.company

import kotlinx.serialization.SerialName
import ru.trfx.catalog.repository.IdEntity
import java.util.*

@kotlinx.serialization.Serializable
data class Company(
    override val name: String,
    @SerialName("country_code") val countryCode: String,
    override val id: Long? = null,
) : IdEntity {
    companion object {
        val COUNTRY_CODES: Array<String> = Locale.getISOCountries()
    }
}
