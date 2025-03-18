package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Расписание-Дни недели (дни оперирования)
object DaysOfOperation : Table(name = "days_of_operation") {
    val id = integer("id").autoIncrement()
    val scheduleId = integer("schedule_id").references(FlightSchedule.id, onDelete = ReferenceOption.CASCADE)
    val dayId = integer("day_id").references(DaysOfTheWeek.id)

    override val primaryKey = PrimaryKey(id)
}