package self.adragon.model.db

import kotlinx.serialization.Serializable

@Serializable
data class Route(val departureAirportID: Int, val arrivalAirportID: Int, val departureDate: Int)
