package com.fabiosanto.weather

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fabiosanto.weather.data.BaseWeatherRepository
import com.fabiosanto.weather.data.WeatherState
import com.fabiosanto.weather.location.LocationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TodayWeatherViewState {
    object Idle : TodayWeatherViewState()
    data class ShowContent(
        @DrawableRes val weatherIcon: Int,
        val description: String,
        val temperature: String, // TODO improve temperature unit C or F ?
    ) : TodayWeatherViewState()
}

// TODO make this injected as hiltViewModel
class TodayWeatherViewModel(
    private val weatherRepository: BaseWeatherRepository
) : ViewModel() {

    private val mutableStateFlow = MutableStateFlow<TodayWeatherViewState>(TodayWeatherViewState.Idle)
    var viewState: StateFlow<TodayWeatherViewState> = mutableStateFlow

    private fun loadWeather(currentLocationData: LocationData) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherData = weatherRepository.getWeather(
                latitude = currentLocationData.latitude,
                longitude = currentLocationData.longitude
            )

            mutableStateFlow.value = weatherData?.let {
                TodayWeatherViewState.ShowContent(
                    temperature = "${it.temperature}°", // TODO improve temperature unit C or F ?
                    weatherIcon = it.state.icon,
                    description = it.state.description
                )
            } ?: noWeatherData
        }
    }

    fun onNewLocationData(locationData: LocationData) {
        loadWeather(locationData)
    }

    // Define ViewModel factory in a companion object
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val weatherRepository = (this[APPLICATION_KEY] as WeatherApplication).weatherRepository
                TodayWeatherViewModel(
                    weatherRepository = weatherRepository
                )
            }
        }
    }
}

// In case something goes wrong while retrieving the data we send an unknown data state
// I decided to don't show previous data as that refers to the previous location.
private val noWeatherData = TodayWeatherViewState.ShowContent(
    temperature = "-",
    weatherIcon = WeatherState.UNKNOWN.icon,
    description = WeatherState.UNKNOWN.description
)