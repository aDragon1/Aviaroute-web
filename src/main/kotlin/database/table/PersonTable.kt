package database.table

import org.jetbrains.exposed.sql.Table


object PersonTable : Table(name = "persons") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val age = integer("age")

    override val primaryKey = PrimaryKey(id)

    fun toJson(id: Int, name: String, age: Int) = """
        {
        "id":$id, 
        "name":"$name", 
        "age":$age
        }
    """.trimIndent()
}

