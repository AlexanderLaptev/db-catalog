package ru.trfx.catalog.response

@kotlinx.serialization.Serializable
data class ErrorResponse(
    val error: String,
)
