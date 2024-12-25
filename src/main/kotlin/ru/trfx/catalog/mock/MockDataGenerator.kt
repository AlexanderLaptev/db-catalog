package ru.trfx.catalog.mock

import io.ktor.util.logging.*
import ru.trfx.catalog.company.Company
import ru.trfx.catalog.company.CompanyRepository
import ru.trfx.catalog.manufacturer.MedicineManufacturerRepository
import ru.trfx.catalog.medicine.Medicine
import ru.trfx.catalog.medicine.MedicineRepository
import ru.trfx.catalog.pharmacy.Pharmacy
import ru.trfx.catalog.pharmacy.PharmacyRepository
import ru.trfx.catalog.stock.Stock
import ru.trfx.catalog.stock.StockRepository
import kotlin.random.Random

object MockDataGenerator {
    private val medicineNames = listOf(
        "Aciclovir", "Amoxicillin", "Aspirin", "Atenolol", "Baclofen", "Betahistine", "Bisacodyl",
        "Bumetanide", "Calcipotriol", "Carvedilol", "Chlorhexidine", "Citalopram", "Clonidine",
        "Codeine", "Cyclizine", "Diazepam", "Diclofenac", "Docusate", "Doxycycline", "Edoxaban",
        "Enalapril", "Esomeprazole", "Fentanyl", "Finasteride", "Furosemide", "Gabapentin", "Gaviscon",
        "Glimepiride", "Haloperidol", "Heparinoid", "Hydrocortisone", "Hydroxocobalamin", "Ibuprofen",
        "Insulin", "Ketoconazole", "Lactulose", "Letrozole", "Lithium", "Losartan", "Macrogol",
        "Melatonin", "Metoprolol", "Morphine", "Naproxen", "Nicorandil", "Nystatin", "Oestrogen",
        "Olmesartan", "Oxybutynin", "Pantoprazole", "Paxlovid", "Pepto-Bismol", "Pravastatin",
        "Propranolol", "Quetiapine", "Ranitidine", "Risedronate", "Ropinirole", "Rosuvastatin", "Senna",
        "Simeticone", "Sotalol", "Sulfasalazine", "Tadalafil", "Tibolone", "Tolterodine", "Tramadol",
        "Tylenol", "Utrogestan", "Valsartan", "Venlafaxine", "Verapamil", "Warfarin", "Zolpidem", "Zopiclone",
    )

    private val companyNames = listOf(
        "AAH", "Abbott", "Acadia", "ACME Laboratories", "Aerie", "Agios", "Alcon", "Annexin", "Astex", "Avax",
        "Barkat", "Baxter", "Beacon", "Benitec", "Biocon", "BioMarin", "Bluepharma", "Bracco",
        "Cadila", "Catalyst", "Century", "Cipla", "Concord", "Corcept", "CytRx", "Cyclacel",
        "Daewoong", "Debiopharm", "Delix", "Douglas",
        "Elder", "Endo", "Endocyte", "Exelgyn",
    )

    private val countries = listOf("RU", "US", "GB", "FR", "CN")

    // TODO: refactor?
    fun generateMockData(logger: Logger) {
        val random = Random(81646)

        for (name in medicineNames) MedicineRepository.create(Medicine(name))
        for (name in companyNames) CompanyRepository.create(Company(name, countries.random(random)))

        var count = medicineNames.size + companyNames.size
        repeat(companyNames.size) {
            val ids = medicineNames.indices.asSequence().shuffled(random).take(random.nextInt(4, 10)).map { it + 1 }
            for (id in ids) {
                MedicineManufacturerRepository.addManufacturer(id.toLong(), (it + 1).toLong())
                count++
            }
        }

        val pharmacyCount = 300
        count += pharmacyCount
        repeat(pharmacyCount) {
            val websiteUrl: String? = if (random.nextBoolean()) {
                "https://pharmacy.example.com/${it + 1}"
            } else {
                null
            }

            val pharmacy = Pharmacy(
                "Pharmacy #${it + 1}",
                random.nextDouble(-90.0, 90.0),
                random.nextDouble(0.0, 180.0),
                websiteUrl,
            )
            PharmacyRepository.create(pharmacy)
        }

        repeat(pharmacyCount) {
            val ids = medicineNames.indices.asSequence().shuffled(random).take(random.nextInt(4, 10)).map { it + 1 }
            val stockCount = if (random.nextFloat() < 0.20) 0 else random.nextInt(1, 30)
            for (id in ids) {
                val stock = Stock(
                    id.toLong(),
                    (it + 1).toLong(),
                    stockCount,
                    random.nextInt(6, 120) * 5.0,
                )
                StockRepository.create(stock)
                count++
            }
        }

        logger.info("Created $count entities total.")
    }
}
