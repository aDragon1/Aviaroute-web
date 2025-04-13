package self.adragon.model.db

data class FlightSchedule(
    val departureAirportId: Int,
    val arrivalAirportId: Int,
    val airlineID: Int,
    val seasonId: Int,
    val estimatedDepartureTime: Long,
    val estimatedArrivalTime: Long,
    val economyPrice: Float,
    val priorityPrice: Float,
    val businessPrice: Float
)
