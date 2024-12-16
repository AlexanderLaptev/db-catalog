package ru.trfx.catalog

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import ru.trfx.catalog.company.Company
import ru.trfx.catalog.company.CompanyRepository
import ru.trfx.catalog.company.CompanyRoutes
import ru.trfx.catalog.company.CompanyTable
import ru.trfx.catalog.medicine.Medicine
import ru.trfx.catalog.medicine.MedicineRepository
import ru.trfx.catalog.medicine.MedicineRoutes
import ru.trfx.catalog.medicine.MedicineTable
import ru.trfx.catalog.pharmacy.PharmacyRoutes
import ru.trfx.catalog.route.AbstractRoutes
import ru.trfx.catalog.webapp.webAppRoutes
import java.util.*

lateinit var db: Database
    private set

private val routes = listOf<AbstractRoutes<*>>(
    MedicineRoutes,
    CompanyRoutes,
    PharmacyRoutes
)

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused")
fun Application.main() {
    install(ContentNegotiation) { json() }
    install(IgnoreTrailingSlash)

    configureDatabase()
    configureTemplating()

    for (route in routes) route.addRoutes(this)
    webAppRoutes()
}

private fun configureDatabase() {
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
        )
    }

    addMockData()
}

private fun addMockData() {
    repeat(200) {
        val m = Medicine("Medicine #${it + 1}")
        MedicineRepository.create(m)
    }

    val country = Locale.getISOCountries()
    repeat(100) {
        val c = Company("Company #${it + 1}", country.random())
        CompanyRepository.create(c)
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
