package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Типы ВС
object AircraftTypes : Table(name = "aircraft_types") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val description = varchar("description", 200)
    val economyTotal = integer("economy_total")
    val priorityTotal = integer("priority_total")
    val businessTotal = integer("business_total")

    override val primaryKey = PrimaryKey(id)
}