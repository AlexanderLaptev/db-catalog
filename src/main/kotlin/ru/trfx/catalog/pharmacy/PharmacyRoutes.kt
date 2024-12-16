package ru.trfx.catalog.pharmacy

import ru.trfx.catalog.route.AbstractRoutes

object PharmacyRoutes : AbstractRoutes<Pharmacy>(
    "pharmacy",
    PharmacyRepository,
    Pharmacy::class,
    Pharmacy.serializer(),
)
