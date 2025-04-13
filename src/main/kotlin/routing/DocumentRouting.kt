package self.adragon.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.*
import self.adragon.DATE_FORMAT
import self.adragon.DateEpochUtils
import self.adragon.database.repository.DocumentRepository
import java.time.format.DateTimeParseException

private const val MAIN_ROUTE = "/documents"
private const val SCHEDULE_ROUTE = "/schedule"

fun Application.configureDocumentsRouting() {

    routing {
        post("$MAIN_ROUTE/$SCHEDULE_ROUTE/monthly_by_tail_number") {
            call.respond(HttpStatusCode.OK, "TODO")
            try {
                val params = call.receiveParameters()

                val date = params["date"]
                val tailNumber = params["tail_number"]

                if (date == null) return@post call.respond(HttpStatusCode.BadRequest, "Дата не указана")
                if (tailNumber == null) return@post call.respond(HttpStatusCode.BadRequest, "Номер рейса не указан")

                val dateEpoch = DateEpochUtils.convertDateToEpoch(date, DATE_FORMAT)
                println(dateEpoch)

            } catch (e: Exception) {
                val result = when (e) {
                    is DateTimeParseException -> mapOf("result" to "Wrong date format. Use $DATE_FORMAT instead")
                    else -> mapOf("result" to "${e.message}")
                }
                println(result)
                call.respond(HttpStatusCode.BadRequest, result)
            }
        }

        // Расписание на день по всем авиарейсам Flights
        post("$MAIN_ROUTE/$SCHEDULE_ROUTE/daily") {
            try {
                val params = call.receiveParameters()
                val date = params["date"]

                if (date == null)
                    return@post call.respond(HttpStatusCode.BadRequest, "Дата не указана")

                val bytes = DocumentRepository.createDailyReport(date)
                call.respondBytes(bytes)

            } catch (e: Exception) {
                val result = when (e) {
                    is DateTimeParseException -> mapOf("result" to "Wrong date format. Use $DATE_FORMAT instead")
                    else -> mapOf("result" to "${e.message}")
                }
                println(result)
                println(e.stackTrace)
                call.respond(HttpStatusCode.BadRequest, result)
            }
        }

        // Расписание по проданным рейсам за период
        post("$MAIN_ROUTE/ticket/report") {
            try {
                val params = call.receiveParameters()

                val startDate = params["start_date"]
                val endDate = params["end_date"]

                if (startDate == null) return@post call.respond(HttpStatusCode.BadRequest, "Дата начала не указана")
                if (endDate == null) return@post call.respond(HttpStatusCode.BadRequest, "Дата конца не указана")

                val startDateEpoch = DateEpochUtils.convertDateToEpoch(startDate, DATE_FORMAT)
                val endDateEpoch = DateEpochUtils.convertDateToEpoch(startDate, DATE_FORMAT)

                println("$startDateEpoch -> $endDateEpoch")

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
