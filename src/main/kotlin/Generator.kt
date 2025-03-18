package self.adragon

import database.table.PersonTable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.json.JSONObject
import self.adragon.database.repository.AirportRepository
import self.adragon.database.repository.CityRepository
import self.adragon.database.repository.CountryRepository
import self.adragon.database.repository.DayOfTheWeekRepository
import self.adragon.database.table.Cities
import self.adragon.database.table.Countries

object Generator {

    private data class Airport(
        val name: String, val iata: String, val icao: String, val country: String, val city: String
    )

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        engine { requestTimeout = 60_000 }
    }

    private val daysOfTheWeek =
        listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")

    suspend fun generate() {
        daysOfTheWeek.forEach { DayOfTheWeekRepository.insert(it) }
        generateAirportsAndCityAndCountries()
    }

    suspend fun generateAirportsAndCityAndCountries() {
        val response = fetchAirports()
        val json = JSONObject(response)

        val rows = json.getJSONArray("rows")
        val airports = mutableListOf<Airport>()
        for (i in 0 until rows.length()) {
            val cur = rows.getJSONObject(i)

            val name = cur.getString("name")
            val city = cur.getString("city")
            val icao = cur.getString("icao")
            val iata = cur.getString("iata")
            val country = cur.getString("country")

            airports.add(Airport(name, iata, icao, country, city))
        }

        val counties = airports.groupBy { it.country }
        val mapped = counties.mapValues { it.value.groupBy { it.city } }

        mapped.keys.forEach { countryKey ->
            val countryId =
                CountryRepository.insert(countryKey).resultedValues?.last()?.get(Countries.id) ?: return@forEach
            val country = mapped[countryKey]

            country?.keys?.forEach { cityKey ->
                val cityId =
                    CityRepository.insert(cityKey, countryId).resultedValues?.last()?.get(Cities.id) ?: return@forEach

                country[cityKey]?.forEach { AirportRepository.insert(it.name, cityId, it.icao, it.iata) }
            }
        }
    }

    // https://rapidapi.com/ntd119/api/flightradar24-com/playground/apiendpoint_6241cd8a-2c35-4680-85e2-446904086c61
    suspend fun fetchAirports(): String {
        val response = client.get("https://flightradar24-com.p.rapidapi.com/v2/airports/list") {
            headers {
                append("x-rapidapi-host", "flightradar24-com.p.rapidapi.com")
                append("x-rapidapi-key", "d3afd2a0f0msh96c5b639c3545f0p11bd96jsn9f8bbba3bf6a")
            }
        }
        client.close()
        return response.bodyAsText()
    }
}