package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.CitiesTable
import self.adragon.database.table.CitiesTable.code
import self.adragon.database.table.CitiesTable.countryID
import self.adragon.database.table.CitiesTable.name
import self.adragon.model.db.City

object CityRepository {
    fun insertMany(cities: List<City>) = transaction {
        try {
            CitiesTable.batchInsert(cities) { city ->
                this[code] = city.code
                this[name] = city.name
                this[countryID] = city.countryId
            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("CityRepository ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }

    fun getIdByCode(code: String) = transaction {
        CitiesTable.select(CitiesTable.id).where { CitiesTable.code eq code }.firstOrNull()?.get(CitiesTable.id)
    }

    fun getAllIdAndCodes() = transaction {
        CitiesTable.select(CitiesTable.id, code).map {
            val id = it[CitiesTable.id]
            val code = it[code]

            Pair(id, code)
        }
    }
}