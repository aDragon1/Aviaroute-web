package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.AirlinesTable
import self.adragon.database.table.AirlinesTable.iataCode
import self.adragon.database.table.AirlinesTable.icaoCode
import self.adragon.database.table.AirlinesTable.name
import self.adragon.model.db.Airline

object AirlineRepository {
    fun insertMany(airlines: List<Airline>) = transaction {
        try {
            AirlinesTable.batchInsert(airlines) { airline ->
                this[icaoCode] = airline.icaoCode
                this[name] = airline.name
                this[iataCode] = airline.iataCode
            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("AirlineRepository ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }

    fun getAllIdsAndNames() = transaction {
        AirlinesTable.select(AirlinesTable.id, name).map {
            val id = it[AirlinesTable.id]
            val name = it[name]

            Pair(id, name)
        }
    }

    fun getAllIds() = transaction {
        AirlinesTable.select(AirlinesTable.id).map { it[AirlinesTable.id] }
    }
}