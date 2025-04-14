package self.adragon.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import self.adragon.database.repository.AirportRepository

private const val MAIN_ROUTE = "/airports"
fun Application.configureAirportRouting() {
    routing {
        get("$MAIN_ROUTE/name_and_ids") {
            call.respond(HttpStatusCode.OK, Json.encodeToString(AirportRepository.getAllIdAndNames()))
        }

        get("$MAIN_ROUTE/location_list") {
            call.respond(HttpStatusCode.OK, Json.encodeToString(AirportRepository.getAllLocations()))
        }
    }
}

