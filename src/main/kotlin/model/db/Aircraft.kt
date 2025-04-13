package self.adragon.model.db

import org.jetbrains.exposed.sql.ResultRow
import self.adragon.database.table.AircraftTable

data class Aircraft(val tailNumber: String, val aircraftTypeId: Int, val airlineId: Int) {
    companion object {
        fun fromRow(row: ResultRow) = Aircraft(
            row[AircraftTable.tailNumber],
            row[AircraftTable.aircraftTypeID],
            row[AircraftTable.airlineID]
        )
    }
}
