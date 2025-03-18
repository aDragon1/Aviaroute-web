package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Воздушные суда
object Aircraft : Table(name = "aircraft") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val aircraftTypeID = integer("aircraft_type_id").references(AircraftTypes.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}