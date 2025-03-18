package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.Aircraft

object AircraftRepository {
    fun insert(pName: String, pAircraftTypeID: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Aircraft.insert {
            it[name] = pName
            it[aircraftTypeID] = pAircraftTypeID
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        Aircraft.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Aircraft.selectAll().where { Aircraft.id eq id }.map { it }
    }.singleOrNull()
}