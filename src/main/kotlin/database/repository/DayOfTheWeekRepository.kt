package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.DaysOfTheWeek

object DayOfTheWeekRepository {
    fun insert(pName: String) = transaction {
        addLogger(StdOutSqlLogger)
        DaysOfTheWeek.insert { it[name] = pName }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        DaysOfTheWeek.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        DaysOfTheWeek.selectAll().where { DaysOfTheWeek.id eq id }.map { it }
    }.singleOrNull()
}