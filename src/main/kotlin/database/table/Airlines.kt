package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Авиакомпании
object Airlines : Table(name = "airlines") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val iataCode = varchar("iata_code", 3)
    val description = varchar("description", 200)

    override val primaryKey = PrimaryKey(id)
}