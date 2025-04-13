package self.adragon.model.documents

import self.adragon.model.AirportLocation

data class DailyReportRow(
    val scheduleId: Int,
    val departureAirport: AirportLocation,
    val depEpoch: Long,
    val arrivalAirport: AirportLocation,
    val arrEpoch: Long,
)
