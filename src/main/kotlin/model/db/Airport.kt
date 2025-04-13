package self.adragon.model.db

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import self.adragon.database.table.AirportsTable

@Serializable
data class Airport(val icaoCode: String, val name: String, val iataCode: String, val cityId: Int) {
    companion object {
        fun fromRow(row: ResultRow): Airport = Airport(
            row[AirportsTable.icaoCode],
            row[AirportsTable.name],
            row[AirportsTable.iataCode],
            row[AirportsTable.cityID]
        )
    }
}
