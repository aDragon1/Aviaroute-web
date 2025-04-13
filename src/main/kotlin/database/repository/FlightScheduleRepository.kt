package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.FlightScheduleTable
import self.adragon.model.db.FlightSchedule

object FlightScheduleRepository {
    fun insertMany(flightSchedules: List<FlightSchedule>) = transaction {
        try {
            FlightScheduleTable.batchInsert(flightSchedules) { schedule ->
                this[FlightScheduleTable.departureAirportID] = schedule.departureAirportId
                this[FlightScheduleTable.arrivalAirportID] = schedule.arrivalAirportId
                this[FlightScheduleTable.seasonID] = schedule.seasonId
                this[FlightScheduleTable.estimatedDepartureTime] = schedule.estimatedDepartureTime
                this[FlightScheduleTable.estimatedArrivalTime] = schedule.estimatedArrivalTime
                this[FlightScheduleTable.economyPrice] = schedule.economyPrice
                this[FlightScheduleTable.priorityPrice] = schedule.priorityPrice
                this[FlightScheduleTable.businessPrice] = schedule.businessPrice
                this[FlightScheduleTable.airlineID] = schedule.airlineID
            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("FlightScheduleRepository insertMany ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }

    fun insertManyWithID(flightSchedules: List<Pair<Int, FlightSchedule>>) = transaction {
        try {
            FlightScheduleTable.batchInsert(flightSchedules) { (i, schedule) ->
                this[FlightScheduleTable.id] = i
                this[FlightScheduleTable.departureAirportID] = schedule.departureAirportId
                this[FlightScheduleTable.arrivalAirportID] = schedule.arrivalAirportId
                this[FlightScheduleTable.seasonID] = schedule.seasonId
                this[FlightScheduleTable.estimatedDepartureTime] = schedule.estimatedDepartureTime
                this[FlightScheduleTable.estimatedArrivalTime] = schedule.estimatedArrivalTime
                this[FlightScheduleTable.economyPrice] = schedule.economyPrice
                this[FlightScheduleTable.priorityPrice] = schedule.priorityPrice
                this[FlightScheduleTable.businessPrice] = schedule.businessPrice
                this[FlightScheduleTable.airlineID] = schedule.airlineID
            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("FlightScheduleRepository insertManyWithID ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }
}