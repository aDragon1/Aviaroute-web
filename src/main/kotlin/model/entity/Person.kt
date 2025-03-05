package model.entity

import kotlinx.serialization.Serializable
import model.table.PersonTable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Person(val id: Int, val name: String, val age: Int) {

    companion object {
        fun fromRow(row: ResultRow) = Person(
            id = row[PersonTable.id],
            name = row[PersonTable.name],
            age = row[PersonTable.age]
        )
    }
}

