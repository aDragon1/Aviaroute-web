package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.Countries

object CountryRepository {
    fun insert(pName: String) = transaction {
        addLogger(StdOutSqlLogger)
        Countries.insert { it[name] = pName }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        Countries.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Countries.selectAll().where { Countries.id eq id }.map { it }
    }.singleOrNull()
}