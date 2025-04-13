package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Станы
object CountriesTable : Table(name = "countries") {
    val id = integer("id").autoIncrement()
    val code = varchar("code", 2).uniqueIndex() // iso2
    val name = text("name")

    override val primaryKey = PrimaryKey(id)
}