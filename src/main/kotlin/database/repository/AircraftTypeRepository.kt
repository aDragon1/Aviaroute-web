package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.AircraftTypesTable
import self.adragon.database.table.AircraftTypesTable.businessTotal
import self.adragon.database.table.AircraftTypesTable.economyTotal
import self.adragon.database.table.AircraftTypesTable.icaoCode
import self.adragon.database.table.AircraftTypesTable.model
import self.adragon.database.table.AircraftTypesTable.priorityTotal
import self.adragon.model.db.AircraftType

object AircraftTypeRepository {
    fun insertMany(aircraftTypes: List<AircraftType>) = transaction {
        try {
            AircraftTypesTable.batchInsert(aircraftTypes) { aircraftType ->
                this[icaoCode] = aircraftType.icaoCode
                this[model] = aircraftType.model
                this[economyTotal] = aircraftType.economy
                this[priorityTotal] = aircraftType.priority
                this[businessTotal] = aircraftType.business

            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("AircraftTypeRepository ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }

    fun selectAll() = transaction {
        AircraftTypesTable.selectAll().map {
            val id = it[AircraftTypesTable.id]
            val aircraftType = AircraftType.fromRow(it)
            id to aircraftType
        }
    }

    fun selectAllIdsAndModels() = transaction {
        AircraftTypesTable.select(AircraftTypesTable.id, model).map {
            val id = it[AircraftTypesTable.id]
            val model = it[model]

            Pair(id, model)
        }
    }

}