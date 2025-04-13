package self.adragon.model.db

import org.jetbrains.exposed.sql.ResultRow
import self.adragon.database.table.AircraftTypesTable

data class AircraftType(
    val icaoCode: String,
    val model: String,
    val economy: Int,
    val priority: Int,
    val business: Int
) {
    companion object {
        fun fromRow(row: ResultRow) = AircraftType(
            row[AircraftTypesTable.icaoCode],
            row[AircraftTypesTable.model],
            row[AircraftTypesTable.economyTotal],
            row[AircraftTypesTable.priorityTotal],
            row[AircraftTypesTable.businessTotal]
        )
    }
}
