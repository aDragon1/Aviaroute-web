package self.adragon.database.repository

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.FlightsTable
import self.adragon.model.ScheduledFlight
import self.adragon.model.db.Flight

object FlightRepository {
    fun insertMany(flights: List<Flight>) = transaction {
        try {
            FlightsTable.batchInsert(flights) { flights ->
                this[FlightsTable.flightNumber] = flights.flightNumber
                this[FlightsTable.aircraftID] = flights.aircraftId
                this[FlightsTable.economyLeft] = flights.economyLeft
                this[FlightsTable.priorityLeft] = flights.priorityLeft
                this[FlightsTable.businessLeft] = flights.businessLeft
                this[FlightsTable.actualDepartureTime] = flights.actualDepartureTime
                this[FlightsTable.flightTime] = flights.flightTime
            }
        } catch (e: ExposedSQLException) {
            println("~".repeat(20))
            println("FlightRepository ERROR: ${e.message}\n")
            println("~".repeat(20))
        }
    }

    fun getFlightByDepartureAndEpoch(
        departureID: Int, dayStartEpoch: Int, dayEndEpoch: Int? = null
    ): List<ScheduledFlight> {
        val q1 = "SELECT * FROM get_flight($departureID, $dayStartEpoch)"
        val q2 = "SELECT * FROM get_flight($departureID, $dayStartEpoch) WHERE dep_time <= $dayEndEpoch"

        return getFlights(if (dayEndEpoch == null) q1 else q2)
    }

    private fun getFlights(q: String) =
        transaction {
            exec(q) { rs ->
                val flights = mutableListOf<ScheduledFlight>()
                while (rs.next()) {
                    val flightID = rs.getInt("flight_id")
                    val arrID = rs.getInt("arr_id")
                    val depTime = rs.getInt("dep_time")
                    val arrTime = rs.getInt("arr_time")

                    val f = ScheduledFlight(flightID, depTime, arrTime, arrID)
                    flights.add(f)
                }
                flights
            } ?: emptyList()
        }
}