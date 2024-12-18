package ru.trfx.catalog.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.trfx.catalog.repository.IdEntity
import java.util.*

@Serializable
data class Company(
    override val name: String,
    @SerialName("country_code") val countryCode: String,
    override val id: Long? = null,
) : IdEntity {
    companion object {
        val COUNTRY_CODES: Array<String> = Locale.getISOCountries()
    }

    override fun validate() {
        require(countryCode.uppercase() in COUNTRY_CODES) { "Country must be a valid 2-letter ISO country" }
    }
}
