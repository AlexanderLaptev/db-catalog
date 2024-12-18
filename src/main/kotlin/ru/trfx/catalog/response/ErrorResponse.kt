package ru.trfx.catalog.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String,
)
