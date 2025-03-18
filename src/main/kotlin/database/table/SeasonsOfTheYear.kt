package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Сезоны года
object SeasonsOfTheYear : Table(name = "seasons_of_the_year") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val description = varchar("description", 200)

    override val primaryKey = PrimaryKey(id)
}