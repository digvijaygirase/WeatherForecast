package com.digvijay.weatherforecast.data

class LocationRepository(private val locationDataSource: LocationDataSource) {

    suspend fun getDeviceLocation() = locationDataSource.getGpsLocation()
}