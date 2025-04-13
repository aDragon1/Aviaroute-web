package self.adragon.database.repository

import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.AirportsTable
import self.adragon.database.table.AirportsTable.cityID
import self.adragon.database.table.AirportsTable.iataCode
import self.adragon.database.table.AirportsTable.icaoCode
import self.adragon.database.table.AirportsTable.name
import self.adragon.model.db.Airport
import self.adragon.model.AirportLocation

object AirportRepository {
    fun insertMany(airports: List<Airport>) = transaction {
        var cur: Airport? = null
        try {
            AirportsTable.batchInsert(airports) { airport ->
                cur = airport
                this[icaoCode] = airport.icaoCode
                this[name] = airport.name
                this[iataCode] = airport.iataCode
                this[cityID] = airport.cityId
            }
        } catch (e: Exception) {
            println("~".repeat(10))
            println("AirportRepository ERROR: ${e.message}\n")
            println(cur)
        }
    }

    fun getAll() = transaction {
        AirportsTable.selectAll().map { Airport.fromRow(it) }
    }

    fun getAllIds() = transaction {
        AirportsTable.select(AirportsTable.id).map { it[AirportsTable.id] }
    }

    fun getAllIdAndNames() = transaction {
        AirportsTable.select(AirportsTable.id, name).map {
            val a = it[AirportsTable.id]
            val b = it[name]

            Pair(a, b)
        }
    }

    fun getLocationByID(id: Int) = transaction {
        exec("SELECT * FROM get_airport_info($id)") { rs ->

            if(!rs.next()) return@exec null
            val country = rs.getString("country_name")
            val city = rs.getString("city_name")
            val airport = rs.getString("airport_name")
            val code = rs.getString("airport_code")

            AirportLocation(id, country, city, airport, code)
        }
    }

    fun getAllLocations(): List<AirportLocation> {
        val query =
            "SELECT a.id, ai.* FROM airports a, LATERAL get_airport_info(a.id) ai ORDER BY country_name, city_name, airport_name;"

        return transaction {
            exec(query) { rs ->
                val locations = mutableListOf<AirportLocation>()
                while (rs.next()) {
                    val airportId = rs.getInt("airport_id")
                    val country = rs.getString("country_name")
                    val city = rs.getString("city_name")
                    val airport = rs.getString("airport_name")
                    val code = rs.getString("airport_code")

                    val loc = AirportLocation(airportId, country, city, airport, code)
                    locations.add(loc)
                }
                locations
            }
        } ?: emptyList()
    }
}