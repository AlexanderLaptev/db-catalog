package ru.trfx.catalog.repository

interface IdEntity {
    val name: String
    val id: Long?

    fun validate() = Unit
}
