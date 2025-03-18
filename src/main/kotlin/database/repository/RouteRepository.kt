package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.Routes

object RouteRepository {
    fun insert(pDepartureAirportId: Int, pArrivalAirportId: Int, pDueDate: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Routes.insert {
            it[departureAirportId] = pDepartureAirportId
            it[arrivalAirportId] = pArrivalAirportId
            it[dueDate] = pDueDate
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        Routes.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Routes.selectAll().where { Routes.id eq id }.map { it }
    }.singleOrNull()
}