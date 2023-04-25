package com.fabiosanto.weather.location

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

data class LocationData(
    val latitude: Double,
    val longitude: Double,
)

interface BaseLocationRepository {
    val locationsFlow: Flow<LocationData>

    fun observeLocationUpdates()
    fun stopLocationUpdates()
}

/**
 * This repository safely observes location updates and
 * pauses emissions when Ui is not visible anymore
 */
class LocationRepository(
    private val lifecycleScope: LifecycleCoroutineScope
) : BaseLocationRepository {
    private val locationProvider = FakeLocationProvider().also {
        it.startFakeLocationUpdates()
    }

    private val mutableFlow = MutableSharedFlow<LocationData>()
    override val locationsFlow: Flow<LocationData> = mutableFlow

    private var observerJob: Job? = null

    override fun observeLocationUpdates() {
        observerJob = lifecycleScope.launch {
            locationProvider.locationsFlow.collect { newLocation ->
                newLocation?.let {
                    mutableFlow.emit(it)
                }
                Log.d("LocationRepository", "observed -> $newLocation")
            }
        }
    }

    /**
     *  Stopping location updates emission as the Ui is not visible anymore
     *  However this won't stop the fake updates which simulates user movements
     */
    override fun stopLocationUpdates() {
        observerJob?.cancel()
    }
}
