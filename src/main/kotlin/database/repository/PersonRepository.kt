package self.adragon.database.repository

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import database.table.PersonTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object PersonRepository {
    fun insert(pName: String, pAge: Int) = transaction {
        addLogger(StdOutSqlLogger)
        PersonTable.insert {
            it[name] = pName
            it[age] = pAge
        }
    }

    fun selectAll() = transaction {
        addLogger(StdOutSqlLogger)
        PersonTable.selectAll().map { it }
    }

    fun selectById(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        PersonTable.selectAll().where { PersonTable.id eq id }.map { it }
    }.singleOrNull()

    fun delete(id: Int) = transaction { PersonTable.deleteWhere { PersonTable.id eq id } }
}

