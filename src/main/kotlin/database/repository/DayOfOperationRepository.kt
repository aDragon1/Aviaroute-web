package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.DaysOfOperationTable

object DayOfOperationRepository {
    fun insertMany(ids: List<Pair<Int, Int>>) = transaction {
        try {
            DaysOfOperationTable.batchInsert(ids) { (schedule, day) ->
                this[DaysOfOperationTable.scheduleId] = schedule
                this[DaysOfOperationTable.dayID] = day
            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("FlightScheduleRepository ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }
}