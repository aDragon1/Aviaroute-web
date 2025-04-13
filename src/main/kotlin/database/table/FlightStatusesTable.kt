package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Статус рейса:
object FlightStatusesTable : Table(name = "flight_statuses") {
    val id = integer("id").autoIncrement()
    val status = varchar("status", 20)

    override val primaryKey = PrimaryKey(id)
}