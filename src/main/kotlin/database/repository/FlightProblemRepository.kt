package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.FlightProblems

object FlightProblemRepository {
    fun insert(pFlightId: Int, pProblem: String) = transaction {
        addLogger(StdOutSqlLogger)
        FlightProblems.insert {
            it[flightId] = pFlightId
            it[problem] = pProblem
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        FlightProblems.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        FlightProblems.selectAll().where { FlightProblems.id eq id }.map { it }
    }.singleOrNull()
}