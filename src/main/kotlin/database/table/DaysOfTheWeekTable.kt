package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Дни недели
object DaysOfTheWeekTable : Table(name = "days_of_the_week") {
    val id = integer("id").autoIncrement()
    val name = text("name").uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}
