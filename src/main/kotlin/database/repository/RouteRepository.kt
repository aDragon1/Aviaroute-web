package self.adragon.database.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.database.repository.SegmentRepository.getFlightSegments
import self.adragon.database.table.RoutesTable
import self.adragon.model.ScheduledFlight
import self.adragon.model.db.Route
import self.adragon.model.db.Segment
import self.adragon.model.routeDetails.FlightSegment

object RouteRepository {

    private fun insertAndGetID(route: Route) = transaction {
        RoutesTable.insert {
            it[departureAirportID] = route.departureAirportID
            it[arrivalAirportID] = route.arrivalAirportID
            it[departureEpoch] = route.departureDate
        }.resultedValues!!.first()[RoutesTable.id]
    }

    private fun getRoutesIDs(depID: Int, arrID: Int, depEpoch: Int) = transaction {
        exec("SELECT * FROM get_routes_ids($depID, $arrID, $depEpoch)") { rs ->
            mutableListOf<Int>().apply { while (rs.next()) add(rs.getInt("route_id")) }
        } ?: emptyList()
    }

    fun getRoute(depID: Int, arrID: Int, depEpoch: Int): List<List<FlightSegment>> {
        var routeIDs = getRoutesIDs(depID, arrID, depEpoch)
        if (routeIDs.isEmpty()) createRoute(depID, arrID, depEpoch)

        val secondTry = getRoutesIDs(depID, arrID, depEpoch)
        routeIDs = if (secondTry.isNotEmpty()) secondTry else return emptyList()
        println("Найденные routeID: $routeIDs")

        return routeIDs.map { routeID -> getFlightSegments(routeID) }
    }

    private fun createRoute(departureID: Int, arrivalID: Int, depEpoch: Int) {
        println("Создание маршрута ($departureID -> $arrivalID) для depEpoch = $depEpoch")
        val segments = mutableListOf<Segment>()

        val segFlights = FlightRepository.getFlightByDepartureAndEpoch(departureID, depEpoch, depEpoch + 86400)

        // 1 segment; A -> B
        val oneSegmentFlights = segFlights.filter { it.arrID == arrivalID }
        println("Найдено прямых рейсов: ${oneSegmentFlights.size}")

        segments.addAll(oneSegmentFlights.map {
            val routeID = insertAndGetID(Route(departureID, arrivalID, depEpoch))
            Segment(routeID, it.flightID, 1)
        })

        // 2 segment; A -> C -> B !! shuchki (Shtuchki) !!
        val twoSegmentFlights = findTwoSegmentRoutes(segFlights, arrivalID)
        println("Найдено маршрутов с 2 сегментами: ${twoSegmentFlights.size}")

        twoSegmentFlights.forEach { (firstFlightID, secondFlightID) ->
            val routeID = insertAndGetID(Route(departureID, arrivalID, depEpoch))
            segments.add(Segment(routeID, firstFlightID, 1))
            segments.add(Segment(routeID, secondFlightID, 2))
        }

        // 3 segment; A -> C -> D -> B
        val threeSegmentFlights = findThreeSegmentRoutes(segFlights, arrivalID)
        println("Найдено маршрутов с 3 сегментами: ${threeSegmentFlights.size}")

        threeSegmentFlights.forEach { (firstFlightID, secondFlightID, thirdFlightID) ->
            val routeID = insertAndGetID(Route(departureID, arrivalID, depEpoch))
            segments.add(Segment(routeID, firstFlightID, 1))
            segments.add(Segment(routeID, secondFlightID, 2))
            segments.add(Segment(routeID, thirdFlightID, 3))
        }

        println("Общее количество сегментов для маршрута ($departureID -> $arrivalID): ${segments.size}")
        SegmentRepository.insertMany(segments)
    }

    private fun findTwoSegmentRoutes(segFlights: List<ScheduledFlight>, targetArrivalID: Int): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()

        segFlights.forEach { seg1 ->
            val segFlights2 = FlightRepository.getFlightByDepartureAndEpoch(seg1.arrID, seg1.arrEpoch)

            segFlights2.filter { seg2 -> seg2.arrID == targetArrivalID }
                .forEach { seg2 -> result.add(Pair(seg1.flightID, seg2.flightID)) }
        }
        return result
    }

    private fun findThreeSegmentRoutes(
        segFlights: List<ScheduledFlight>,
        targetArrivalID: Int,
    ): List<Triple<Int, Int, Int>> {
        val result = mutableListOf<Triple<Int, Int, Int>>()

        segFlights.forEach { seg1 ->
            val segFlights2 = FlightRepository.getFlightByDepartureAndEpoch(seg1.arrID, seg1.arrEpoch)

            segFlights2.forEach { seg2 ->
                val segFlights3 = FlightRepository.getFlightByDepartureAndEpoch(seg2.arrID, seg2.arrEpoch)

                segFlights3.filter { seg3 -> seg3.arrID == targetArrivalID }
                    .forEach { seg3 -> result.add(Triple(seg1.flightID, seg2.flightID, seg3.flightID)) }
            }
        }
        return result
    }
}