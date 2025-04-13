package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.CountriesTable
import self.adragon.database.table.CountriesTable.code
import self.adragon.database.table.CountriesTable.name
import self.adragon.model.db.Country

object CountryRepository {
    fun insertMany(countries: List<Country>) = transaction {
        addLogger(StdOutSqlLogger)
        try {
            CountriesTable.batchInsert(countries) { country ->
                this[code] = country.iso2
                this[name] = country.name
            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("CountryRepository ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }

    fun getAllIdAndCodes() = transaction {
        CountriesTable.select(CountriesTable.id, code).map {
            val id = it[CountriesTable.id]
            val code = it[code]

            Pair(id, code)
        }
    }

    fun getIdByCode(code: String) = transaction {
        CountriesTable.select(CountriesTable.id).where { CountriesTable.code eq code }.firstOrNull()
            ?.get(CountriesTable.id)
    }
}