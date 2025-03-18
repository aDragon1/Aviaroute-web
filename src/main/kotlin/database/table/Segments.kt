package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Сегменты
object Segments : Table(name = "segments") {
    val id = integer("id").autoIncrement()
    val routeId = integer("route_id").references(Routes.id, onDelete = ReferenceOption.CASCADE)
    val flightId = integer("flight_id").references(Flights.id, onDelete = ReferenceOption.CASCADE)
    val position = integer("position")

    override val primaryKey = PrimaryKey(id)
}