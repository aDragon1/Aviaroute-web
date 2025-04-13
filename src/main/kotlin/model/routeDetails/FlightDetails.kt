package self.adragon.model.routeDetails

import kotlinx.serialization.Serializable

@Serializable
data class FlightDetails(
    val tailNumber: String,
    val economy: CabinClassInfo,
    val priority: CabinClassInfo,
    val business: CabinClassInfo,
)


