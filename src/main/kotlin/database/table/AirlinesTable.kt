package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Авиакомпании
object AirlinesTable : Table(name = "airlines") {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val icaoCode = varchar("icao_code", 3).uniqueIndex()
    val iataCode = varchar("iata_code", 3)

    override val primaryKey = PrimaryKey(id)
}