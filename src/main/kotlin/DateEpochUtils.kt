package self.adragon

import self.adragon.model.SeasonOfTheYear
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

const val DATE_FORMAT = "dd.MM.yyyy"
const val DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm"
const val TIME_FORMAT = "HH:mm"

object DateEpochUtils {
    fun convertDateToEpoch(dateStr: String, format: String): Int {
        val formatter = DateTimeFormatter.ofPattern(format)
        val date = LocalDate.parse(dateStr, formatter)
        val zoneId = ZoneId.of("Europe/Moscow")

        return date.atStartOfDay(zoneId).toEpochSecond().toInt()
    }

    fun convertEpochSecondsStringDateTime(epochSeconds: Long?, format: String): String {
        if (epochSeconds == null) return ""
        val formatter = DateTimeFormatter.ofPattern(format)

        val instantOfEpoch = Instant.ofEpochSecond(epochSeconds)
        val instantAsLocalDateTime =
            instantOfEpoch.atZone(ZoneId.systemDefault()).toLocalDateTime()
        return instantAsLocalDateTime.format(formatter)
    }

    fun getSeasonFromEpoch(epochSeconds: Long, zoneId: ZoneId = ZoneId.of("UTC")): SeasonOfTheYear {
        val date = Instant.ofEpochSecond(epochSeconds).atZone(zoneId).toLocalDate()
        val month = date.monthValue

        return when (month) {
            in 0..2 -> SeasonOfTheYear.WINTER
            in 3..5 -> SeasonOfTheYear.SPRING
            in 6..8 -> SeasonOfTheYear.SUMMER
            else -> SeasonOfTheYear.AUTUMN
        }
    }

    fun getNextDateForDayOfWeek(dayOfWeek: Int): ZonedDateTime {
        // Получаем текущую дату и время
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        var nextDate = now.toLocalDate()

        // Находим ближайший день недели (если сегодня нужный день, он тоже подходит)
        while (nextDate.dayOfWeek.value != dayOfWeek) nextDate = nextDate.plusDays(1)

        // Конвертируем в ZonedDateTime
        return nextDate.atStartOfDay(now.zone)
    }

}