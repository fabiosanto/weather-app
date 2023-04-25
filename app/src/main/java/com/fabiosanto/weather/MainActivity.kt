package com.fabiosanto.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.fabiosanto.weather.location.LocationRepository
import com.fabiosanto.weather.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    // TODO make these injected with hilt
    private val locationRepository: LocationRepository = LocationRepository(lifecycleScope)
    private val viewModel: TodayWeatherViewModel by viewModels { TodayWeatherViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            WeatherAppTheme {
                // Defaults from Android Studio
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodayWeatherScreen(
                        viewModel = viewModel
                    )
                }
            }
        }

        collectLocationUpdates()
    }

    private fun collectLocationUpdates() {
        lifecycleScope.launch {
            locationRepository.locationsFlow.collect {
                viewModel.onNewLocationData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        locationRepository.observeLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        locationRepository.stopLocationUpdates()
    }
}
