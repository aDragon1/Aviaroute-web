package self.adragon.model.routeDetails

import kotlinx.serialization.Serializable
import self.adragon.model.AirportLocation

@Serializable
data class FlightSegment(
    val departureAirportLocation: AirportLocation,
    val arrivalAirportLocation: AirportLocation,
    val departureEpoch: Int,
    val arrivalEpoch: Int,
    val details: FlightDetails,
    val position: Int,
)

