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
import ru.trfx.catalog.medicine.Medicine
import ru.trfx.catalog.medicine.MedicineRepository
import ru.trfx.catalog.medicine.MedicineTable
import ru.trfx.catalog.medicine.medicineRoutes
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

    configureDatabase()
    configureTemplating()

    medicineRoutes()
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
        )
    }

    addMockData()
}

private fun addMockData() {
    repeat(200) {
        val m = Medicine("Medicine #${it + 1}")
        MedicineRepository.save(m)
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
