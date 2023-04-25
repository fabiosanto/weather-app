package com.fabiosanto.weather.data

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

data class WeatherResponse(
    val temperature: Int,
    val state: WeatherState,
)

interface BaseWeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse?
}

class WeatherRepository : BaseWeatherRepository {
    private val client = OkHttpClient()

    // Flow emits locations
    //https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true

    private val baseUrl = "https://api.open-meteo.com/v1/forecast"

    // In case we are unable to retrieve data we return an empty WeatherReponse
    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse? {
        return try {
            val response = run("$baseUrl?latitude=$latitude&longitude=$longitude&current_weather=true")
            val jsonResponse = JSONObject(response)
            val currentWeather = jsonResponse.getJSONObject("current_weather")

            WeatherResponse(
                temperature = currentWeather.getInt("temperature"),
                state = getWeatherCodeData(currentWeather.getInt("weathercode")),
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun getWeatherCodeData(code: Int): WeatherState {
        return WeatherState.values().find { codes ->
            codes.codes.contains(code)
        } ?: WeatherState.UNKNOWN
    }

    // TODO handle http exceptions
    private fun run(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body!!.string()
        }
    }
}