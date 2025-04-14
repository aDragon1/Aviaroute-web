package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.AircraftTable
import self.adragon.database.table.AircraftTable.tailNumber
import self.adragon.database.table.AircraftTable.aircraftTypeID
import self.adragon.database.table.AircraftTable.airlineID
import self.adragon.model.db.Aircraft

object AircraftRepository {
    fun insertMany(aircraft: List<Aircraft>) = transaction {
        try {
            AircraftTable.batchInsert(aircraft) { aircraft ->
                this[tailNumber] = aircraft.tailNumber
                this[aircraftTypeID] = aircraft.aircraftTypeId
                this[airlineID] = aircraft.airlineId
            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("AircraftRepository ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }

    fun selectAll() = transaction { AircraftTable.selectAll().map { Aircraft.fromRow(it) } }

    fun selectAllByAirlineID(airlineID: Int) = transaction {
        AircraftTable.selectAll().where { AircraftTable.airlineID eq AircraftTable.airlineID }
            .map {
                val id = it[AircraftTable.id]
                val aircraft = Aircraft.fromRow(it)
                id to aircraft
            }
    }

    fun getCount() = transaction { AircraftTable.selectAll().count() }
}