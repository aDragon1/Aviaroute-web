package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.Cities

object CityRepository {
    fun insert(pName: String, pCountryId: Int) = transaction {
//        addLogger(StdOutSqlLogger)
        Cities.insert {
            it[name] = pName
            it[countryID] = pCountryId
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        Cities.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Cities.selectAll().where { Cities.id eq id }.map { it }
    }.singleOrNull()
}