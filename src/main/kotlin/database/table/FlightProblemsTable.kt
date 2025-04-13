package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Проблемы по ходу рейса
object FlightProblemsTable : Table(name = "flight_problems") {
    val id = integer("id").autoIncrement()
    val flightID = integer("flight_id").references(FlightsTable.id, onDelete = ReferenceOption.CASCADE)
    val problem = text("problem")

    override val primaryKey = PrimaryKey(id)
}