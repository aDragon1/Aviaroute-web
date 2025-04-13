package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Сезоны года
object SeasonsOfTheYearTable : Table(name = "seasons_of_the_year") {
    val id = integer("id").autoIncrement()
    val name = text("name").uniqueIndex()
    val description = text("description")

    override val primaryKey = PrimaryKey(id)
}