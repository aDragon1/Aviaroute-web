package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Расписание-Дни недели (дни оперирования)
object DaysOfOperationTable : Table(name = "days_of_operation") {
    val id = integer("id").autoIncrement()
    val scheduleId = integer("schedule_id").references(FlightScheduleTable.id, onDelete = ReferenceOption.CASCADE)
    val dayID = integer("day_id").references(DaysOfTheWeekTable.id)

    override val primaryKey = PrimaryKey(id)
}