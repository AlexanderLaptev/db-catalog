package ru.trfx.catalog.company

import ru.trfx.catalog.repository.IdEntity
import java.util.*

@kotlinx.serialization.Serializable
data class Company(
    override val name: String,
    val countryCode: String,
    override val id: Long? = null,
) : IdEntity {
    companion object {
        val COUNTRY_CODES: Array<String> = Locale.getISOCountries()
    }

    init {
        require(countryCode in COUNTRY_CODES) { "Country code must be a valid ISO-3166 alpha-2 code" }
    }
}
