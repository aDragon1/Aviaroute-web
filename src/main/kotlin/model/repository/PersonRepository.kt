package model.services

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import model.entity.Person
import model.table.PersonTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger

object PersonRepository {
    fun insert(pName: String, pAge: Int) = transaction {
        addLogger(StdOutSqlLogger)
        PersonTable.insert {
            it[name] = pName
            it[age] = pAge
        }
    }

    fun selectAll() = transaction { PersonTable.selectAll().map { Person.fromRow(it) } }

    fun selectById(id: Int) = transaction {
        PersonTable.selectAll().where { PersonTable.id eq id }.map { Person.fromRow(it) }
    }.singleOrNull()
}

