package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.AircraftTypes

object AircraftTypeRepository {
    fun insert(pName: String, pDescription: String, pEconomyTotal: Int, pPriorityTotal: Int, pBusinessTotal: Int) =
        transaction {
            addLogger(StdOutSqlLogger)
            AircraftTypes.insert {
                it[name] = pName
                it[description] = pDescription
                it[economyTotal] = pEconomyTotal
                it[priorityTotal] = pPriorityTotal
                it[businessTotal] = pBusinessTotal
            }
        }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        AircraftTypes.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        AircraftTypes.selectAll().where { AircraftTypes.id eq id }.map { it }
    }.singleOrNull()
}