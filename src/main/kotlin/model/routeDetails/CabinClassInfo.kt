package self.adragon.model.routeDetails

import kotlinx.serialization.Serializable

@Serializable
data class CabinClassInfo(
    val seatsLeft: Int,
    val price: Float,
)