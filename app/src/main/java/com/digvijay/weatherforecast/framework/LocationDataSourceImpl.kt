package com.digvijay.weatherforecast.framework

import android.location.Location
import com.digvijay.weatherforecast.data.LocationDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationDataSourceImpl(
    private val locationUpdateManager: LocationUpdateManager,
    private val callback:(Location?)->Unit
) : LocationDataSource {

    override suspend fun getGpsLocation() = locationUpdateManager.getUpdatedLocation(callback)
}