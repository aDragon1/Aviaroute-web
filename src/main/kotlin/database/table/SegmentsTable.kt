package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Сегменты
object SegmentsTable : Table(name = "segments") {
    val id = integer("id").autoIncrement()
    val routeID = integer("route_id").references(RoutesTable.id, onDelete = ReferenceOption.CASCADE)
    val flightID = integer("flight_id").references(FlightsTable.id, onDelete = ReferenceOption.CASCADE)
    val position = integer("position")

    override val primaryKey = PrimaryKey(id)
}