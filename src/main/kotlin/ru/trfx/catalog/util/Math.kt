package ru.trfx.catalog.util

fun clamp(value: Int, min: Int, max: Int) = if (value > max) max else if (value < min) min else value
