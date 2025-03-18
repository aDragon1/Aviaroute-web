package self.adragon.model

import kotlinx.serialization.Serializable
import database.table.PersonTable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Person(val id: Int, val name: String, val age: Int)