package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Проблемы по ходу рейса
object FlightProblems : Table(name = "flight_problems") {
    val id = integer("id").autoIncrement()
    val flightId = integer("flight_id").references(Flights.id, onDelete = ReferenceOption.CASCADE)
    val problem = varchar("problem", 200)

    override val primaryKey = PrimaryKey(id)
}