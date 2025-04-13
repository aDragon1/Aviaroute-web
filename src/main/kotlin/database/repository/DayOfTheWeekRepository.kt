package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.DaysOfOperationTable
import self.adragon.database.table.DaysOfTheWeekTable

object DayOfTheWeekRepository {
    fun insert(pName: String) = transaction {
        try {
            DaysOfTheWeekTable.insert { it[name] = pName }
        } catch (e: ExposedSQLException) {
            println("DayOfTheWeekRepository ERROR: ${e.message}\n")
        }
    }

    fun insertMany(strings: List<String>) = transaction {
        DaysOfTheWeekTable.batchInsert(strings) {
            this[DaysOfTheWeekTable.name] = it
        }
    }

    fun getAllIDs() = transaction { DaysOfTheWeekTable.select(DaysOfTheWeekTable.id).map { it[DaysOfTheWeekTable.id] } }
}