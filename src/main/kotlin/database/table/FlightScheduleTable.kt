package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Расписание авиарейсов
object FlightScheduleTable : Table(name = "flight_schedule") {
    val id = integer("id").autoIncrement()
    val departureAirportID = integer("departure_airport_id").references(AirportsTable.id, ReferenceOption.CASCADE)
    val arrivalAirportID = integer("arrival_airport_id").references(AirportsTable.id, ReferenceOption.CASCADE)
    val seasonID = integer("season_id").references(SeasonsOfTheYearTable.id, ReferenceOption.CASCADE)
    val estimatedDepartureTime = long("estimated_departure_time")
    val estimatedArrivalTime = long("estimated_arrival_time")
    val economyPrice = float("economy_price")
    val priorityPrice = float("priority_price")
    val businessPrice = float("business_price")
    val airlineID = integer("airline_id").references(AirlinesTable.id, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}