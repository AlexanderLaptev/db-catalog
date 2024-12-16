package ru.trfx.catalog.company

import java.util.*

@kotlinx.serialization.Serializable
data class Company(
    val name: String,
    val countryCode: String,
    val id: Long? = null,
) {
    companion object {
        private val COUNTRY_CODES: Array<String> = Locale.getISOCountries()
    }

    init {
        require(countryCode in COUNTRY_CODES) { "Country code must be a valid ISO-3166 alpha-2 code" }
    }
}
