package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Аэропорты
object AirportsTable : Table(name = "airports") {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val icaoCode = varchar("icao_code", 4).uniqueIndex()
    val iataCode = varchar("iata_code", 3)
    val cityID = integer("city_id").references(CitiesTable.id, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}