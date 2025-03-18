package self.adragon.database.table

import org.jetbrains.exposed.sql.Table

// Дни недели
object DaysOfTheWeek : Table(name = "days_of_the_week") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id)
}
