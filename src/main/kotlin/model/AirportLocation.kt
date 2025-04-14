package self.adragon.model

import kotlinx.serialization.Serializable

@Serializable
data class AirportLocation(
    val airportId: Int,
    val countryName: String,
    val cityName: String,
    val airportName: String,
    val airportCode: String
)

