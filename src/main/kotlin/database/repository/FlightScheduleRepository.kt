package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.FlightSchedule

object FlightScheduleRepository {
    fun insert(
        pDepartureAirportId: Int,
        pArrivalAirportId: Int,
        pAirlineId: Int,
        pSeasonId: Int,
        pDepartureTime: Int,
        pArrivalTime: Int,
        pEconomyPrice: Float,
        pPriorityPrice: Float,
        pBusinessPrice: Float
    ) = transaction {
        addLogger(StdOutSqlLogger)
        FlightSchedule.insert {
            it[departureAirportId] = pDepartureAirportId
            it[arrivalAirportId] = pArrivalAirportId
            it[airlineId] = pAirlineId
            it[seasonId] = pSeasonId
            it[estimatedDepartureTime] = pDepartureTime
            it[estimatedArrivalTime] = pArrivalTime
            it[economyPrice] = pEconomyPrice
            it[priorityPrice] = pPriorityPrice
            it[businessPrice] = pBusinessPrice
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        FlightSchedule.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        FlightSchedule.selectAll().where { FlightSchedule.id eq id }.map { it }
    }.singleOrNull()
}