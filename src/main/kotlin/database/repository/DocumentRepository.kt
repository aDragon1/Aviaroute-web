package self.adragon.database.repository

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import org.jetbrains.exposed.sql.transactions.transaction
import self.adragon.DATE_FORMAT
import self.adragon.DATE_TIME_FORMAT
import self.adragon.DateEpochUtils
import self.adragon.model.AirportLocation
import self.adragon.model.documents.DailyReportRow
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.ZoneId

object DocumentRepository {
    private var countAircraftReport = 1

    fun createDailyReport(dateStart: String, dateEnd: String): ByteArray {
        val dateStartEpoch = DateEpochUtils.convertDateToEpoch(dateStart, DATE_FORMAT)
        val dateEndEpoch = DateEpochUtils.convertDateToEpoch(dateEnd, DATE_FORMAT) - 1
        val reportRows = getDailyReport(dateStartEpoch, dateEndEpoch)

        return createDailyReportFile(reportRows, dateStart, dateEnd).toByteArray() ?: byteArrayOf()
    }

    fun createSmallAircraftReport(): ByteArray {
        val smallReport = getSmallAircraftReport()

        val airlinesCounts = AirlineRepository.getCount()
        val aircraftCount = AircraftRepository.getCount()

        val now = LocalDate.now(ZoneId.of("Europe/Moscow")).toString()


        return createSmallAircraftFile(smallReport, airlinesCounts, aircraftCount, now).toByteArray() ?: byteArrayOf()
    }

    private fun createSmallAircraftFile(
        smallReport: List<Pair<String, Long>>, airlinesCounts: Long, aircraftCount: Long, now: String
    ): ByteArrayOutputStream {
        val outputStream = ByteArrayOutputStream()
        val document = Document(PageSize.A4)

        val writer = PdfWriter.getInstance(document, outputStream)
        document.open()

        val titleFont = Font(Font.HELVETICA, 20f, Font.BOLD)

        // Заголовок справки
        val title =
            Paragraph("АО «Авиаперевозки»\nСправка\n№ ${countAircraftReport++}/ВС от $now", titleFont).apply {
                alignment = Element.ALIGN_LEFT
                spacingAfter = 15f
            }

        val subtitle = Paragraph("О зарегистрированных воздушных судах в информационной системе").apply {
            alignment = Element.ALIGN_LEFT
            spacingAfter = 20f
        }
        val intro = Paragraph(
            "По состоянию на $now в системе зарегистрировано $aircraftCount воздушных судов, принадлежащих $airlinesCounts авиакомпаниям.\n\n" +
                    "Распределение воздушных судов по моделям представлено в таблице ниже:"
        ).apply { spacingAfter = 15f }

        document.add(title)
        document.add(subtitle)
        document.add(intro)

        // Таблица
        val table = PdfPTable(3)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1f, 6f, 2f))

        val headers = listOf("№", "Модель воздушного судна", "Количество")
        headers.forEach { h ->
            PdfPCell(Phrase(h)).apply {
                backgroundColor = Color.lightGray
                horizontalAlignment = Element.ALIGN_CENTER
                table.addCell(this)
            }
        }

        smallReport.forEachIndexed { i, (model, count) ->
            table.addCell(PdfPCell(Phrase("${i + 1}")).apply { horizontalAlignment = Element.ALIGN_CENTER })
            table.addCell(PdfPCell(Phrase(model)).apply { horizontalAlignment = Element.ALIGN_CENTER })
            table.addCell(PdfPCell(Phrase("$count")).apply { horizontalAlignment = Element.ALIGN_CENTER })
        }

        document.add(table)

        // Заключение
        val outro = Paragraph(
            "\nСправка составлена для внутреннего использования и может быть представлена по требованию контролирующих органов.\n\n" +
                    "Начальник службы авиационного реестра\n________________ /Егорова А.А./\n\nМ.П."
        )
        outro.spacingBefore = 30f
        document.add(outro)

        val cb = writer.directContent
        val rectX = 470f  // X-координата
        val rectY = 100f  // Y-координата (от нижнего края страницы)
        val rectWidth = 80f
        val rectHeight = 40f

        cb.rectangle(rectX, rectY, rectWidth, rectHeight)
        cb.stroke()

        document.close()
        writer.close()

        return outputStream
    }

    private fun createDailyReportFile(
        rows: List<DailyReportRow>,
        dateStart: String,
        dateEnd: String
    ): ByteArrayOutputStream {
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

        val titleSuffix = if (dateStart == dateEnd) dateStart else "период $dateStart - $dateEnd"
        val title = Paragraph("Расписание всех рейсов на $titleSuffix", titleFont).apply {
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
        rows.forEach { r -> createRow(r, cellFont, DATE_TIME_FORMAT).forEach { cell -> table.addCell(cell) } }

        table.setWidths(FloatArray(headers.size) { 1f })
        table.defaultCell.isNoWrap = true

        document.add(table)

        document.close()
        writer.close()

        return outputStream
    }

    private fun getDailyReport(dateStartEpoch: Int, dateEndEpoch: Int) = transaction {
        exec("SELECT * FROM get_period_flight_schedule($dateStartEpoch, $dateEndEpoch);") { rs ->
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

    private fun getSmallAircraftReport() = transaction {
        exec("SELECT * FROM get_aircraft_type_summary()") { rs ->
            val smallAircraft = mutableListOf<Pair<String, Long>>()
            while (rs.next()) {
                val model = rs.getString("aircraft_model")
                val count = rs.getLong("aircraft_count")

                smallAircraft.add(model to count)
            }
            smallAircraft
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