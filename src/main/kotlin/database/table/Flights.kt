package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Авиарейсы
object Flights : Table(name = "flights") {
    val id = integer("id").autoIncrement()
    val flightNumber = integer("flight_number").references(FlightSchedule.id, onDelete = ReferenceOption.CASCADE)
    val aircraftId = integer("aircraft_id").references(Aircraft.id, onDelete = ReferenceOption.CASCADE)
    val economyLeft = integer("economy_left")
    val priorityLeft = integer("priority_left")
    val businessLeft = integer("business_left")
    val actualDepartureTime = integer("actual_departure_time")
    val flightTime = integer("flight_time")

    override val primaryKey = PrimaryKey(id)
}
