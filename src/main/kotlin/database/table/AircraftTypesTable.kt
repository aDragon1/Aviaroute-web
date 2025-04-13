package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Типы ВС
object AircraftTypesTable : Table(name = "aircraft_types") {
    val id = integer("id").autoIncrement()
    val icaoCode = varchar("icao_code", 4).uniqueIndex()
    val model = text("model")
    val economyTotal = integer("economy_total")
    val priorityTotal = integer("priority_total")
    val businessTotal = integer("business_total")

    override val primaryKey = PrimaryKey(id)
}