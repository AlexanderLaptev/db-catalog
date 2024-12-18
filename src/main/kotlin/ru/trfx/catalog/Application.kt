package ru.trfx.catalog

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import io.ktor.util.logging.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import ru.trfx.catalog.company.CompanyRoutes
import ru.trfx.catalog.company.CompanyTable
import ru.trfx.catalog.manufacturer.MedicineManufacturerTable
import ru.trfx.catalog.manufacturer.medicineManufacturerRoutes
import ru.trfx.catalog.medicine.MedicineRoutes
import ru.trfx.catalog.medicine.MedicineTable
import ru.trfx.catalog.mock.MockDataGenerator
import ru.trfx.catalog.pharmacy.PharmacyRoutes
import ru.trfx.catalog.pharmacy.PharmacyTable
import ru.trfx.catalog.route.AbstractRoutes
import ru.trfx.catalog.stock.StockTable
import ru.trfx.catalog.stock.stockRoutes
import ru.trfx.catalog.webapp.webAppRoutes

lateinit var db: Database
    private set


fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused")
fun Application.main() {
    install(ContentNegotiation) { json() }
    install(IgnoreTrailingSlash)

    configureDatabase(log)
    configureTemplating()

    val routes = listOf<AbstractRoutes<*>>(
        MedicineRoutes,
        CompanyRoutes,
        PharmacyRoutes
    )
    for (route in routes) route.addRoutes(this)

    medicineManufacturerRoutes()
    stockRoutes()
    webAppRoutes()
}

private fun configureDatabase(logger: Logger) {
    db = Database.connect(
        arrayOf(
            "jdbc:h2:mem:regular",
            "MODE=PostgreSQL",
            "DATABASE_TO_LOWER=TRUE",
            "DEFAULT_NULL_ORDERING=HIGH",
            "DB_CLOSE_DELAY=-1",
        ).joinToString(";"),
        "org.h2.Driver"
    )

    transaction {
        SchemaUtils.create(
            MedicineTable,
            CompanyTable,
            PharmacyTable,
            MedicineManufacturerTable,
            StockTable,
        )

        MockDataGenerator.generateMockData(logger)
    }
}

private fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
}
