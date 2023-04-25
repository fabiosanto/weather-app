package com.fabiosanto.weather

import app.cash.turbine.test
import com.fabiosanto.weather.data.WeatherState
import com.fabiosanto.weather.location.LocationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * testing TodayWeatherViewModelTest
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TodayWeatherViewModelTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `whenever ViewModel is instantiated then valid weather data is shown`() = runTest {
        val fakeWeatherRepository = FakeWeatherRepository()
        val todayWeatherViewModel = TodayWeatherViewModel(fakeWeatherRepository)

        todayWeatherViewModel.viewState.test {

            todayWeatherViewModel.onNewLocationData(LocationData(0.0, 0.0))

            Assert.assertEquals(awaitItem(), TodayWeatherViewState.Idle)
            Assert.assertEquals(
                awaitItem(), TodayWeatherViewState.ShowContent(
                    weatherIcon = WeatherState.SUNNY.icon,
                    description = WeatherState.SUNNY.description,
                    temperature = "3°"
                )
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}