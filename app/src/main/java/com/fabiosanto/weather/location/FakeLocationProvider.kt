package com.fabiosanto.weather.location

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * This provider represents the user timeline of locations updates
 * The accepted assumption is that this doesn't ever stop emitting as it replicates the user location changes (walking for example)
 */
class FakeLocationProvider : CoroutineScope {
    private var lastLocationIndex = -1
    private var fakeEmissionsJob: Job? = null

    // Flow emits locations
    private val mutableFlow = MutableStateFlow<LocationData?>(null)
    private val locationUpdateFrequency = 10.seconds
    var locationsFlow: Flow<LocationData?> = mutableFlow

    internal fun startFakeLocationUpdates() {
        fakeEmissionsJob = launch {
            while (isActive) {
                val nextLocation = getNextLocation()
                Log.d("FakeLocationProvider", "moving... $lastLocationIndex $nextLocation")
                mutableFlow.emit(nextLocation)
                delay(locationUpdateFrequency)
            }
        }
    }

    private fun getNextLocation(): LocationData {
        lastLocationIndex++

        if (lastLocationIndex > fakeTimeline.lastIndex) {
            lastLocationIndex = 0
        }
        return fakeTimeline[lastLocationIndex]
    }

    override val coroutineContext = SupervisorJob() + Dispatchers.Default
}

private val fakeTimeline = arrayListOf(
    LocationData(53.619653, 10.079969),
    LocationData(53.080917, 8.847533),
    LocationData(52.378385, 9.794862),
    LocationData(52.496385, 13.444041),
    LocationData(53.866865, 10.739542),
    LocationData(54.304540, 10.152741),
    LocationData(54.797277, 9.491039),
    LocationData(52.426412, 10.821392),
    LocationData(53.542788, 8.613462),
    LocationData(53.141598, 8.242565)
)