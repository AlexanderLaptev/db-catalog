package ru.trfx.catalog.company

import ru.trfx.catalog.route.AbstractRoutes

object CompanyRoutes : AbstractRoutes<Company>(
    "company",
    CompanyRepository,
    Company::class,
    Company.serializer(),
)
