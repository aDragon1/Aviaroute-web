package self.adragon.model.db

import database.table.PersonTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Person(val id: Int, val name: String, val age: Int) {
    companion object {
        fun fromRow(row: ResultRow): Person = Person(row[PersonTable.id], row[PersonTable.name], row[PersonTable.age])
    }
}