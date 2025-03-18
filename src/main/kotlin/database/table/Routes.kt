package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Маршруты
object Routes : Table(name = "routes") {
    val id = integer("id").autoIncrement()
    val departureAirportId = integer("departure_airport_id").references(Airports.id, onDelete = ReferenceOption.CASCADE)
    val arrivalAirportId = integer("arrival_airport_id").references(Airports.id, onDelete = ReferenceOption.CASCADE)
    val dueDate = integer("due_date")

    override val primaryKey = PrimaryKey(id)
}
