package ru.trfx.catalog.medicine

import ru.trfx.catalog.route.AbstractRoutes

object MedicineRoutes : AbstractRoutes<Medicine>(
    "medicine",
    MedicineRepository,
    Medicine::class,
    Medicine.serializer(),
)
