package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Маршруты
object RoutesTable : Table(name = "routes") {
    val id = integer("id").autoIncrement()
    val departureAirportID = integer("departure_airport_id").references(AirportsTable.id, ReferenceOption.CASCADE)
    val arrivalAirportID = integer("arrival_airport_id").references(AirportsTable.id, ReferenceOption.CASCADE)
    val departureEpoch = integer("departure_epoch")

    override val primaryKey = PrimaryKey(id)
}
