package ru.trfx.catalog.response

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<out T>(
    @SerialName("last_page") val lastPage: Long,
    @SerialName("last_row") val lastRow: Long,
    val data: List<@Contextual T>,
)
