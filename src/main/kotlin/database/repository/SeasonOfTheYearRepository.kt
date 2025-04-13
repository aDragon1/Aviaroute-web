package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.SeasonsOfTheYearTable
import self.adragon.model.SeasonOfTheYear

object SeasonOfTheYearRepository {
    fun insert(pName: String, pDescription: String) = transaction {
        addLogger(StdOutSqlLogger)
        try {
            SeasonsOfTheYearTable.insert {
                it[name] = pName
                it[description] = pDescription
            }
        } catch (e: ExposedSQLException) {
            println("SeasonOfTheYearRepository ERROR: ${e.message}\n")
        }
    }

    fun getAll() = transaction {
        SeasonsOfTheYearTable.selectAll().map {
            val seasonID = it[SeasonsOfTheYearTable.id]
            val seasonName = it[SeasonsOfTheYearTable.name]

            val seasonEnum = when (seasonName) {
                SeasonOfTheYear.WINTER.displayName -> SeasonOfTheYear.WINTER
                SeasonOfTheYear.SPRING.displayName -> SeasonOfTheYear.SPRING
                SeasonOfTheYear.AUTUMN.displayName -> SeasonOfTheYear.AUTUMN
                SeasonOfTheYear.SUMMER.displayName -> SeasonOfTheYear.SUMMER
                else -> SeasonOfTheYear.ERROR
            }

            seasonID to seasonEnum
        }
    }
}