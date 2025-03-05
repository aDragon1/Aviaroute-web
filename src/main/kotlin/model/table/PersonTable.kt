package model.table

import org.jetbrains.exposed.sql.Table


object PersonTable : Table(name = "persons") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 10)
    val age = integer("age")

    override val primaryKey = PrimaryKey(id)
}

