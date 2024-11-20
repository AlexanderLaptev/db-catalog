package ru.trfx.catalog.model

import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.charLength
import org.jetbrains.exposed.sql.trim

object Companies : IntIdTable("\"company\"") {
    val name = varchar("name", 256)
    val country = char("country", 2).check { it.trim().charLength() eq 2 }
}

class Company(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Company>(Companies)

    val name by Companies.name
    val country by Companies.country
}

object Medicines : IntIdTable("\"medicine\"") {
    val name = varchar("name", 256)
}

class Medicine(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Medicine>(Medicines)

    val name by Medicines.name
}

object PharmacyChains : IntIdTable("\"pharmacy_chain\"") {
    val name = varchar("name", 256)
    val website = varchar("website", 2048).nullable()
}

class PharmacyChain(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PharmacyChain>(PharmacyChains)

    val name by PharmacyChains.name
    val website by PharmacyChains.website
}

object Pharmacies : IntIdTable("\"pharmacy\"") {
    val chainId = optReference("chain_id", PharmacyChains.id)
    val latitude = float("latitude")
    val longitude = float("longitude")
}

class Pharmacy(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Pharmacy>(Pharmacies)

    val chain by PharmacyChain optionalReferencedOn Pharmacies.chainId
    val latitude by Pharmacies.latitude
    val longitude by Pharmacies.longitude
}

object MedicineManufacturers : CompositeIdTable("\"medicine_manufacturer\"") {
    val medicineId = reference("medicine_id", Medicines.id).entityId()
    val companyId = reference("company_id", Companies.id).entityId()

    override val primaryKey = PrimaryKey(medicineId, companyId)
}

class MedicineManufacturer(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<MedicineManufacturer>(MedicineManufacturers)

    val medicine by Medicine referencedOn MedicineManufacturers.medicineId
    val company by Company referencedOn MedicineManufacturers.companyId
}

object Stocks : CompositeIdTable("\"stock\"") {
    val medicineId = reference("medicine_id", Medicines.id).entityId()
    val pharmacyId = reference("pharmacy_id", Pharmacies.id).entityId()
    val count = integer("count").default(0).check { it greater 0 }
    val price = float("price").check { it greater 0.0f }

    override val primaryKey = PrimaryKey(medicineId, pharmacyId)
}

class Stock(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<Stock>(Stocks)

    val medicine by Medicine referencedOn Stocks.medicineId
    val pharmacy by Pharmacy referencedOn Stocks.pharmacyId
    val count by Stocks.count
    val price by Stocks.price
}
