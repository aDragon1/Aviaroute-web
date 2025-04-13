package self.adragon

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.json.JSONObject
import self.adragon.database.repository.*
import self.adragon.model.SeasonOfTheYear
import self.adragon.model.db.*
import java.io.File
import kotlin.ranges.random
import kotlin.time.measureTime

object Generator {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        engine { requestTimeout = 60_000 }
    }

    private val daysOfTheWeek =
        listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")

    private val seasons = listOf(
        SeasonOfTheYear.WINTER to "Холодно",
        SeasonOfTheYear.SPRING to "Менее холодно",
        SeasonOfTheYear.SUMMER to "Жарко",
        SeasonOfTheYear.AUTUMN to "Менее жарко"
    )
    private val statuses = listOf("Регистрация", "В полете", "Завершен", "Отменен")

    fun generate() {
        beautyCall("Flight status") { generateFlightStatus() }
        beautyCall("Days of the week") { generateDayOfTheWeeks() }
        beautyCall("Seasons of the year") { generateSeasonOfTheYear() }
        beautyCall("aircraft types") { getAircraftTypes() }
        beautyCall("countries") { getCountries("countries") }
        beautyCall("airlines") { getAirlines("airlines") }
        beautyCall("cities") { getCities("cities") }
        beautyCall("airports") { getAirports("airports") }
        beautyCall("airplanes") { getAirplanes() }
        beautyCall("schedule") { getSchedule() }
        beautyCall("flights") { getFlights() }
    }

    private fun beautyCall(name: String, action: () -> Unit) {
        println("=".repeat(40))
        println("Generating '$name':")
        println("   Done in ${measureTime { action() }}")
        println("=".repeat(40))
        println()
    }

    // My own genius
    private fun getFlights() = transaction { exec("call insert_from_schedule_to_flights();") }

    // My own genius
    fun getSchedule() {
        val airportIDs = AirportRepository.getAllIds()
        val airlineIDs = AirlineRepository.getAllIds()
        val dayOfTheWeekIDs = DayOfTheWeekRepository.getAllIDs()
        val seasonsOfTheYear = SeasonOfTheYearRepository.getAll()

        val minFlightTime = 2 * 3600L
        val maxFlightTime = 10 * 3600L

        val weeksToGenerate = 26 // 26 недель (полгода)

        var flightScheduleIndex = 1

        // Генерируем все возможные пары аэропортов
        val airportPairs = airportIDs.flatMap { depCode ->
            airportIDs.filter { it != depCode }.map { arrCode -> depCode to arrCode }
        }
        println("AirportPairs size - ${airportPairs.size}")

        val flightSchedules = mutableListOf<Pair<Int, FlightSchedule>>()
        val daysOfOperation = mutableListOf<Pair<Int, Int>>()

        dayOfTheWeekIDs.forEach { day ->
            airportPairs.forEach { (dep, arr) ->
                val airlineID = airlineIDs.random()

                val priorCoefficient = (1..3).random()
                val businessCoefficient = (5..8).random()
                val economy = (1000..10000).random().toFloat()
                val prior = economy * priorCoefficient.toFloat()
                val bus = economy * businessCoefficient.toFloat()

                val initialDate = DateEpochUtils.getNextDateForDayOfWeek(day)
                repeat(weeksToGenerate) { offset ->
                    // Генерируем случайное время в течение дня
                    val randomHour = (0..23).random()
                    val randomMinute = (0..59).random()
                    val departureDateTime = initialDate.withHour(randomHour).withMinute(randomMinute)
                        .withSecond(0).withNano(0).plusWeeks(offset.toLong())

                    val depEpoch = departureDateTime.toEpochSecond()

                    val flightDuration = (minFlightTime..maxFlightTime).random()
                    val arrEpoch = depEpoch + flightDuration

                    val season = seasonsOfTheYear.firstOrNull {
                        it.second == DateEpochUtils.getSeasonFromEpoch(depEpoch)
                    }?.first ?: return@repeat

                    val fs = FlightSchedule(dep, arr, airlineID, season, depEpoch, arrEpoch, economy, prior, bus)

                    flightSchedules.add(flightScheduleIndex to fs)
                    daysOfOperation.add(flightScheduleIndex to day)
                    flightScheduleIndex++
                }
            }
        }
        println("FlightSchedules / DaysOfOperation size - ${flightSchedules.size}")
        FlightScheduleRepository.insertManyWithID(flightSchedules)
        DayOfOperationRepository.insertMany(daysOfOperation)
    }


    // My own genius
    private fun getAirplanes() {
        fun generateRegistrationNumber(): String {
            val letters = ('A'..'Z').shuffled().take(2).joinToString("")
            val numbers = (1000..9999).random()

            return "$letters$numbers"
        }

        val airlineAircraftCountLimit = 7

        // Both list is an int-string (id-name/model) pairs
        val airlineIdAndNames = AirlineRepository.getAllIdsAndNames()
        val aircraftTypeIdAndModels = AircraftTypeRepository.selectAllIdsAndModels()

        val aircrafts = airlineIdAndNames.flatMap { airlineID ->
            val n = (1..airlineAircraftCountLimit).random()
            List(n) {
                val aircraft = aircraftTypeIdAndModels.random()

                val tailNumber = generateRegistrationNumber()

                Aircraft(tailNumber, aircraft.first, airlineID.first)
            }
        }

        AircraftRepository.insertMany(aircrafts)
    }

    // Generated by ChatGPT
    private fun getAircraftTypes() {
        val aircrafts = listOf(
            AircraftType("A320", "Airbus A320", 160, 10, 12),
            AircraftType("B737", "Boeing 737", 180, 12, 10),
            AircraftType("B777", "Boeing 777-200", 314, 30, 25),
            AircraftType("B787", "Boeing 787-9", 250, 30, 15),
            AircraftType("A330", "Airbus A330-300", 280, 35, 20),
            AircraftType("A350", "Airbus A350-900", 320, 35, 35),
            AircraftType("A321", "Airbus A321", 200, 12, 15),
            AircraftType("B767", "Boeing 767-300", 230, 25, 12),
            AircraftType("B757", "Boeing 757-200", 210, 15, 12),
            AircraftType("CRJ9", "Bombardier CRJ-900", 80, 5, 6),
            AircraftType("E190", "Embraer E190", 110, 8, 8),
            AircraftType("E195", "Embraer E195", 130, 10, 8),
            AircraftType("A340", "Airbus A340-300", 280, 35, 20),
            AircraftType("B747", "Boeing 747-400", 450, 40, 30),
            AircraftType("A318", "Airbus A318", 110, 8, 8),
            AircraftType("A319", "Airbus A319", 130, 12, 10),
            AircraftType("ATR7", "ATR 72", 75, 4, 0),
            AircraftType("F100", "Fokker 100", 95, 6, 6)
        )

        AircraftTypeRepository.insertMany(aircrafts)
    }

    //    https://aviationstack.com/documentation#airlines
    private fun getAirlines(endpoint: String) {
        val json = fetchAviationStackAPIRequest(endpoint)
        val data = json.getJSONArray("data")

        val airlines = mutableListOf<Airline>()
        repeat(data.length()) { i ->
            data.getJSONObject(i).run {
                if (getStringOrNull("status") != "active") return@run

                val icaoCode = getStringOrNull("icao_code") ?: return@run
                val iataCode = getStringOrNull("iata_code") ?: ""
                val airlineName = getStringOrNull("airline_name") ?: return@run

                if (icaoCode.length > 3) return@run
                airlines.add(Airline(icaoCode, airlineName, iataCode))
            }
        }

        AirlineRepository.insertMany(airlines.distinctBy { it.icaoCode })
    }

    //    https://aviationstack.com/documentation#airports
    private fun getAirports(endpoint: String) {
        val json = fetchAviationStackAPIRequest(endpoint)
        val data = json.getJSONArray("data")

        val idAndCodes = CityRepository.getAllIdAndCodes().associate { it.second to it.first }

        val airports = mutableListOf<Airport>()
        repeat(data.length()) { i ->
            data.getJSONObject(i).run {
                val icaoCode = getStringOrNull("icao_code") ?: return@run
                val iataCode = getStringOrNull("iata_code") ?: ""
                val cityIataCode = getStringOrNull("city_iata_code") ?: return@run
                val airportName = getStringOrNull("airport_name") ?: return@run

                val cityId = if (idAndCodes.contains(cityIataCode)) idAndCodes[cityIataCode]!! else return@run
                airports.add(Airport(icaoCode, airportName, iataCode, cityId))
            }
        }
        println("   Airports size - ${airports.size}")

        AirportRepository.insertMany(airports.distinctBy { it.icaoCode })
    }

    //    https://aviationstack.com/documentation#cities
    private fun getCities(endpoint: String) {
        val json = fetchAviationStackAPIRequest(endpoint)
        val data = json.getJSONArray("data")

        val idAndCodes = CountryRepository.getAllIdAndCodes().associate { it.second to it.first }

        val cities = mutableListOf<City>()
        repeat(data.length()) { i ->
            data.getJSONObject(i).run {
                val iataCode = getStringOrNull("iata_code") ?: return@run
                val cityName = getStringOrNull("city_name") ?: return@run
                val countryIso2 = getStringOrNull("country_iso2") ?: return@run

                if (iataCode.length > 3) return@run

                val countryId = if (idAndCodes.contains(countryIso2)) idAndCodes[countryIso2]!! else return@run
                cities.add(City(iataCode, cityName, countryId))
            }
        }
        println("   Cities size - ${cities.size}")
        CityRepository.insertMany(cities.distinctBy { it.code })
    }

    //    https://aviationstack.com/documentation#countries
    private fun getCountries(endpoint: String) {
        val json = fetchAviationStackAPIRequest(endpoint)

        val data = json.getJSONArray("data")
        val countries = (0 until data.length()).mapNotNull { i ->
            data.getJSONObject(i).run {
                val countryIso2 = getStringOrNull("country_iso2") ?: return@mapNotNull null
                val name = getStringOrNull("country_name") ?: return@mapNotNull null

                Country(name, countryIso2)
            }
        }
        println("   Countries size - ${countries.size}")
        CountryRepository.insertMany(countries)
    }

    private fun fetchAviationStackAPIRequest(endpoint: String): JSONObject {
        return JSONObject(getEndpointViaFile(endpoint))
    }

    private fun getEndpointViaFile(endpoint: String): String {
        val f = File("src/main/resources/jsons/small/$endpoint.json")

        return f.readText()
    }

    private suspend fun getEndpointViaGet(endpoint: String): String {
        val response =
            client.get("https://api.aviationstack.com/v1/$endpoint?access_key=0ce7c128b1484d997d7afb347abc4ae5&limit=100000")
        client.close()

        return response.bodyAsText()
    }

    private fun generateDayOfTheWeeks() = daysOfTheWeek.forEach { DayOfTheWeekRepository.insert(it) }
    private fun generateSeasonOfTheYear() =
        seasons.forEach { SeasonOfTheYearRepository.insert(it.first.displayName, it.second) }

    private fun generateFlightStatus() = statuses.forEach { FlightStatusRepository.insert(it) }
    private fun JSONObject.getStringOrNull(key: String) = if (this.isNull(key)) null else this.getString(key)
}
