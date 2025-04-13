package self.adragon.database.repository

import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.table.SegmentsTable
import self.adragon.model.routeDetails.FlightDetails
import self.adragon.model.routeDetails.FlightSegment
import self.adragon.model.AirportLocation
import self.adragon.model.db.Segment
import self.adragon.model.routeDetails.CabinClassInfo

object SegmentRepository {
    fun insertMany(segments: List<Segment>) = transaction {
        SegmentsTable.batchInsert(segments) { segment ->
            this[SegmentsTable.routeID] = segment.routeID
            this[SegmentsTable.flightID] = segment.flightID
            this[SegmentsTable.position] = segment.position
        }
    }

    fun getFlightSegments(routeID: Int) = transaction {
        exec("SELECT * FROM get_route_details($routeID);") { rs ->
            val segments = mutableListOf<FlightSegment>()

            while (rs.next()) {
                val tailNumber = rs.getString("tail_number")

                val depAirportID = rs.getInt("departure_airport_id")
                val depCountryName = rs.getString("departure_country_name")
                val depCityName = rs.getString("departure_city_name")
                val depAirportName = rs.getString("departure_airport_name")
                val depAirportCode = rs.getString("departure_airport_code")

                val arrAirportID = rs.getInt("arrival_airport_id")
                val arrCountryName = rs.getString("arrival_country_name")
                val arrCityName = rs.getString("arrival_city_name")
                val arrAirportName = rs.getString("arrival_airport_name")
                val arrAirportCode = rs.getString("arrival_airport_code")

                val depEpoch = rs.getInt("estimated_departure_time")
                val arrEpoch = rs.getInt("estimated_arrival_time")

                val ecLeft = rs.getInt("economy_left")
                val priorLeft = rs.getInt("priority_left")
                val busLeft = rs.getInt("business_left")

                val ecPrice = rs.getFloat("economy_price")
                val priorPrice = rs.getFloat("priority_price")
                val busPrice = rs.getFloat("business_price")

                val segPos = rs.getInt("segment_position")

                val depLoc = AirportLocation(depAirportID, depCountryName, depCityName, depAirportName, depAirportCode)
                val arrLoc = AirportLocation(arrAirportID, arrCountryName, arrCityName, arrAirportName, arrAirportCode)

                val economy = CabinClassInfo(ecLeft, ecPrice)
                val priority = CabinClassInfo(priorLeft, priorPrice)
                val business = CabinClassInfo(busLeft, busPrice)

                val details = FlightDetails(tailNumber, economy, priority, business)

                val seg = FlightSegment(depLoc, arrLoc, depEpoch, arrEpoch, details, segPos)
                segments.add(seg)
            }
            segments
        } ?: emptyList()
    }.sortedBy { it.position }
}