package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Расписание авиарейсов
object FlightSchedule : Table(name = "flight_schedule") {
    val id = integer("id").autoIncrement()
    val departureAirportId = integer("departure_airport_id").references(Airports.id, onDelete = ReferenceOption.CASCADE)
    val arrivalAirportId = integer("arrival_airport_id").references(Airports.id, onDelete = ReferenceOption.CASCADE)
    val airlineId = integer("airline_id").references(Airlines.id, onDelete = ReferenceOption.CASCADE)
    val seasonId = integer("season_id").references(SeasonsOfTheYear.id, onDelete = ReferenceOption.CASCADE)
    val estimatedDepartureTime = integer("estimated_departure_time")
    val estimatedArrivalTime = integer("estimated_arrival_time")
    val economyPrice = float("economy_price")
    val priorityPrice = float("priority_price")
    val businessPrice = float("business_price")

    override val primaryKey = PrimaryKey(id)
}