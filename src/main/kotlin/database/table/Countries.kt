package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Станы
object Countries : Table(name = "countries") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id)
}