package com.fabiosanto.weather.data

import androidx.annotation.DrawableRes
import com.fabiosanto.weather.R

/**
 * Get this strings description translated
 * Either get it from backend or pass it via the resources
 */
enum class WeatherState(
    val codes: Array<Int>,
    val description: String,
    @DrawableRes val icon: Int = R.drawable.ic_question_mark_24,
) {
    UNKNOWN(
        arrayOf(-1),
        "Unknown"
    ),
    SUNNY(
        arrayOf(0),
        "Clear sky",
        R.drawable.ic_sunny_24
    ),
    PARTLY_CLOUDY(
        arrayOf(1, 2, 3),
        "Mainly clear, partly cloudy, and overcast",
        R.drawable.ic_cloudy_24
    ),
    FOG(
        arrayOf(45, 48),
        "Fog and depositing rime fog",
        R.drawable.ic_fog_24
    ),
    DRIZZLE(
        arrayOf(51, 53, 55),
        "Drizzle: Light, moderate, and dense intensity",
        R.drawable.ic_rain_24
    ),
    RAINY(
        arrayOf(61, 63, 65),
        "Rain: Slight, moderate and heavy intensity",
        R.drawable.ic_rain_24
    )
    // Add all others
}
