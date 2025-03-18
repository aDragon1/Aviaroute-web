package self.adragon.database.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.Airlines

object AirlineRepository {
    fun insert(pName: String, pDescription: String) = transaction {
        addLogger(StdOutSqlLogger)
        Airlines.insert {
            it[name] = pName
            it[description] = pDescription
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        Airlines.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Airlines.selectAll().where { Airlines.id eq id }.map { it }
    }.singleOrNull()
}