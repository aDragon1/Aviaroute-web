package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Воздушные суда
object AircraftTable : Table(name = "aircraft") {
    val id = integer("id").autoIncrement()
    val tailNumber = varchar("tail_number", 10) // регистрационный номер
    val aircraftTypeID = integer("aircraft_type_id").references(AircraftTypesTable.id, ReferenceOption.CASCADE)
    val airlineID = integer("airline_id").references(AirlinesTable.id, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}