package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.Airports

object AirportRepository {
    fun insert(pName: String, pCityId: Int, pIcaoCode: String,pIataCode: String) = transaction {
//        addLogger(StdOutSqlLogger)
        Airports.insert {
            it[name] = pName
            it[cityID] = pCityId
            it[icaoCode] = pIcaoCode
            it[iataCode] = pIataCode
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        Airports.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Airports.selectAll().where { Airports.id eq id }.map { it }
    }.singleOrNull()
}