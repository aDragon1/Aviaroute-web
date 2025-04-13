package self.adragon.model.db

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import self.adragon.database.table.FlightsTable

@Serializable
data class Flight(
    val flightNumber: Int,
    val aircraftId: Int,
    val economyLeft: Int,
    val priorityLeft: Int,
    val businessLeft: Int,
    val actualDepartureTime: Long,
    val flightTime: Long,
    val flightStatus: Int
) {
    companion object {
        fun fromRow(row: ResultRow): Flight = Flight(
            row[FlightsTable.flightNumber],
            row[FlightsTable.aircraftID],
            row[FlightsTable.economyLeft],
            row[FlightsTable.priorityLeft],
            row[FlightsTable.businessLeft],
            row[FlightsTable.actualDepartureTime],
            row[FlightsTable.flightTime],
            row[FlightsTable.flightStatus],
        )
    }
}