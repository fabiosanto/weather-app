package com.fabiosanto.weather

import com.fabiosanto.weather.data.BaseWeatherRepository
import com.fabiosanto.weather.data.WeatherResponse
import com.fabiosanto.weather.data.WeatherState

class FakeWeatherRepository : BaseWeatherRepository {

    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        return WeatherResponse(
            temperature = 3,
            state = WeatherState.SUNNY
        )
    }
}