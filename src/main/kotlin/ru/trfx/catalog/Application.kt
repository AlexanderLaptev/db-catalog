package ru.trfx.catalog

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.trfx.catalog.medicine.MedicineTable
import ru.trfx.catalog.medicine.medicineRoutes

lateinit var db: Database
    private set

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused")
fun Application.main() {
    install(ContentNegotiation) { json() }
    configureDatabase()

    medicineRoutes()
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
}
