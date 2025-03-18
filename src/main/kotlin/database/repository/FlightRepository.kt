package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.Flights

object FlightRepository {
    fun insert(
        pFlightNumber: Int,
        pAircraftId: Int,
        pEconomyLeft: Int,
        pPriorityLeft: Int,
        pBusinessLeft: Int,
        pActualDepartureTime: Int,
        pFlightTime: Int
    ) = transaction {
        addLogger(StdOutSqlLogger)
        Flights.insert {
            it[flightNumber] = pFlightNumber
            it[aircraftId] = pAircraftId
            it[economyLeft] = pEconomyLeft
            it[priorityLeft] = pPriorityLeft
            it[businessLeft] = pBusinessLeft
            it[actualDepartureTime] = pActualDepartureTime
            it[flightTime] = pFlightTime
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        Flights.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Flights.selectAll().where { Flights.id eq id }.map { it }
    }.singleOrNull()
}