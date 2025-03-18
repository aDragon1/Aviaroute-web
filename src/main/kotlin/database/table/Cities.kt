package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Города
object Cities : Table(name = "cities") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val countryID =  integer("country_id").references(Countries.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}