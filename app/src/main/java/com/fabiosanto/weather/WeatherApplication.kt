package com.fabiosanto.weather

import android.app.Application
import com.fabiosanto.weather.data.WeatherRepository

class WeatherApplication : Application() {

    // TODO Inject with hilt has singleton
    val weatherRepository = WeatherRepository()
}