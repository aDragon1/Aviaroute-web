package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Авиарейсы
object FlightsTable : Table(name = "flights") {
    val id = integer("id").autoIncrement()
    val flightNumber =
        integer("flight_number").references(FlightScheduleTable.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val aircraftID = integer("aircraft_id").references(AircraftTable.id, onDelete = ReferenceOption.CASCADE)
    val economyLeft = integer("economy_left")
    val priorityLeft = integer("priority_left")
    val businessLeft = integer("business_left")
    val actualDepartureTime = long("actual_departure_time")
    val flightTime = long("flight_time")
    val flightStatus = integer("flight_status").references(FlightStatusesTable.id, ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(id)
}
