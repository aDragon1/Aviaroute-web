package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.DaysOfOperation

object DayOfOperationRepository {
    fun insert(pScheduleId: Int, pDayId: Int) = transaction {
        addLogger(StdOutSqlLogger)
        DaysOfOperation.insert {
            it[scheduleId] = pScheduleId
            it[dayId] = pDayId
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        DaysOfOperation.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        DaysOfOperation.selectAll().where { DaysOfOperation.id eq id }.map { it }
    }.singleOrNull()
}