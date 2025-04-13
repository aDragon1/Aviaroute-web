package self.adragon.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import self.adragon.DateEpochUtils
import self.adragon.database.repository.RouteRepository
import java.time.format.DateTimeParseException
import kotlinx.serialization.json.Json
import self.adragon.DATE_FORMAT

private const val MAIN_ROUTE = "/flights"
fun Application.configureFlightRouting() {
    routing {
        post("$MAIN_ROUTE/search_by_airports_and_date") {
            try {
                val params = call.receiveParameters()
                val departureID = params["departure_id"]?.toIntOrNull() ?: return@post call.respond(
                    HttpStatusCode.BadRequest, "Аэропорт отправления не указан"
                )
                val arrivalID = params["arrival_id"]?.toIntOrNull() ?: return@post call.respond(
                    HttpStatusCode.BadRequest, "Аэропорт назначения не указан"
                )
                val departureDate = params["departure_date"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest, "Дата отправления не указана"
                )
                val departureEpoch = DateEpochUtils.convertDateToEpoch(departureDate, DATE_FORMAT)

                val flights = RouteRepository.getRoute(departureID, arrivalID, departureEpoch)
                val resultJson = Json.encodeToString(flights)

                call.respond(HttpStatusCode.OK, resultJson)
            } catch (e: Exception) {
                val result = when (e) {
                    is DateTimeParseException -> mapOf("result" to "Wrong date format. Use $DATE_FORMAT instead")
                    else -> mapOf("result" to "${e.message}")
                }
                println(result)
                call.respond(HttpStatusCode.BadRequest, result)
            }
        }
    }
}
