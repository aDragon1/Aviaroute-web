package self.adragon.database.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// Города
object CitiesTable : Table(name = "cities") {
    val id = integer("id").autoIncrement()
    val code = varchar("code", 3) // iata code (aviationStack memes) (not exists in real world)
    val name = text("name")
    val countryID = integer("country_id").references(CountriesTable.id, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}