package self.adragon.database.repository

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.DATE_FORMAT
import self.adragon.DateEpochUtils
import self.adragon.TIME_FORMAT
import self.adragon.model.AirportLocation
import self.adragon.model.documents.DailyReportRow
import java.awt.Color
import java.io.ByteArrayOutputStream

object DocumentRepository {
    fun createDailyReport(date: String): ByteArray {
        val dayStartEpoch = DateEpochUtils.convertDateToEpoch(date, DATE_FORMAT)
        val dayEndEpoch = dayStartEpoch + 86_400 - 1
        val reportRows = getDailyReport(dayStartEpoch, dayEndEpoch)

        return createDailyReportFile(reportRows, date).toByteArray() ?: byteArrayOf()
    }

    private fun createDailyReportFile(rows: List<DailyReportRow>, date: String): ByteArrayOutputStream {
        fun createRow(row: DailyReportRow, cellFont: Font, timeFormat: String): List<PdfPCell> {
            val depTime = DateEpochUtils.convertEpochSecondsStringDateTime(row.depEpoch, timeFormat)
            val arrTime = DateEpochUtils.convertEpochSecondsStringDateTime(row.arrEpoch, timeFormat)

            val departurePoint = "${row.departureAirport.countryName}, ${row.departureAirport.cityName}"
            val departureAirport = "${row.departureAirport.airportName}, ${row.departureAirport.airportCode}"

            val arrivalPoint = "${row.arrivalAirport.countryName}, ${row.arrivalAirport.cityName}"
            val arrivalAirport = "${row.arrivalAirport.airportName}, ${row.arrivalAirport.airportCode}"


            return listOf(
                createCell("${row.scheduleId}", cellFont),

                createCell(depTime, cellFont),
                createCell(departurePoint, cellFont),
                createCell(departureAirport, cellFont),

                createCell(arrivalPoint, cellFont),
                createCell(arrivalAirport, cellFont),
                createCell(arrTime, cellFont)
            )
        }

        val headers = listOf(
            "ID расписания",
            "Время вылета",
            "Пункт отправления",
            "Аэропорт отправления",
            "Пункт назначения",
            "Аэропорт назначения",
            "Время прилета"
        )

        val outputStream = ByteArrayOutputStream()
        val marginLeftRight = 20f
        val marginTopBottom = 20f
        val document =
            Document(PageSize.A4.rotate(), marginLeftRight, marginLeftRight, marginTopBottom, marginTopBottom)

        val writer = PdfWriter.getInstance(document, outputStream)
        document.open()

        val titleFont = Font(Font.HELVETICA, 20f, Font.BOLD)
        val title = Paragraph("Расписание всех рейсов на $date", titleFont).apply {
            alignment = Element.ALIGN_CENTER
            spacingAfter = 15f
        }
        document.add(title)

        val table = PdfPTable(headers.size).apply {
            widthPercentage = 100f
            setSpacingBefore(10f)
            setSpacingAfter(10f)
            setWidths(FloatArray(headers.size) { 1f })
        }

        val headerFont = Font(Font.HELVETICA, 16f, Font.BOLD, Color.BLACK)
        val cellFont = Font(Font.HELVETICA, 11f, Font.BOLD)

        headers.forEach { h -> table.addCell(createFontCell(h, headerFont)) }
        rows.forEach { r -> createRow(r, cellFont, TIME_FORMAT).forEach { cell -> table.addCell(cell) } }

        table.setWidths(FloatArray(headers.size) { 1f })
        table.defaultCell.isNoWrap = true

        document.add(table)

        document.close()
        writer.close()

        return outputStream
    }

    private fun getDailyReport(dayStartEpoch: Int, dayEndEpoch: Int) = transaction {
        exec("SELECT * FROM get_period_flight_schedule($dayStartEpoch, $dayEndEpoch);") { rs ->
            val rows = mutableListOf<DailyReportRow>()

            while (rs.next()) {
                val scheduleId = rs.getInt("schedule_id")

                val departureAirportId = rs.getInt("departure_airport_id")
                val departureAirportName = rs.getString("departure_airport_name")
                val departureAirportIcaoCode = rs.getString("departure_airport_icao_code")
                val departureCityName = rs.getString("departure_city_name")
                val departureCountryName = rs.getString("departure_country_name")
                val depEpoch = rs.getLong("dep_epoch")

                val arrivalAirportId = rs.getInt("arrival_airport_id")
                val arrivalAirportName = rs.getString("arrival_airport_name")
                val arrivalAirportIcaoCode = rs.getString("arrival_airport_icao_code")
                val arrivalCityName = rs.getString("arrival_city_name")
                val arrivalCountryName = rs.getString("arrival_country_name")
                val arrEpoch = rs.getLong("arr_epoch")

                val departureLocation = AirportLocation(
                    departureAirportId, departureCountryName, departureCityName,
                    departureAirportName, departureAirportIcaoCode
                )
                val arrivalLocation = AirportLocation(
                    arrivalAirportId, arrivalCountryName, arrivalCityName,
                    arrivalAirportName, arrivalAirportIcaoCode
                )
                val row = DailyReportRow(scheduleId, departureLocation, depEpoch, arrivalLocation, arrEpoch)
                rows.add(row)
            }
            rows
        } ?: emptyList()
    }

    private fun createCell(text: String, cellFont: Font) = PdfPCell(Phrase(text, cellFont)).apply {
        horizontalAlignment = Element.ALIGN_CENTER
        verticalAlignment = Element.ALIGN_MIDDLE
        fixedHeight = 50f
        setPadding(6f)
    }


    private fun createFontCell(text: String, cellFont: Font) = PdfPCell(Phrase(text, cellFont)).apply {
        horizontalAlignment = Element.ALIGN_CENTER
        verticalAlignment = Element.ALIGN_MIDDLE
        backgroundColor = Color(220, 220, 220)
        setPadding(8f)
    }
}