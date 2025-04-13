package self.adragon.model

data class ScheduledFlight(
    val flightID: Int,
    val depEpoch: Int,
    val arrEpoch: Int,
    val arrID: Int,
)
