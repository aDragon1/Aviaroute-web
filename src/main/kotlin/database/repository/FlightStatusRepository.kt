package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.FlightStatusesTable

object FlightStatusRepository {
    fun insert(pStatus: String) = transaction {
        try {
            FlightStatusesTable.insert { it[status] = pStatus }
        } catch (e: ExposedSQLException) {
            println("FlightStatusRepository ERROR: ${e.message}\n")
        }
    }
}