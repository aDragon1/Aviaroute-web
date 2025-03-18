package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Аэропорты
object Airports : Table(name = "airports") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val icaoCode = varchar("icao_code", 4)
    val iataCode = varchar("iata_code", 3)
    val cityID = integer("city_id").references(Cities.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}