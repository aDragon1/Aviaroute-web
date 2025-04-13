package database.table

import org.jetbrains.exposed.sql.Table


object PersonTable : Table(name = "persons") {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val age = integer("age")

    override val primaryKey = PrimaryKey(id)
}

