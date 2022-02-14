package com.digvijay.weatherforecast.data

interface LocationDataSource {

    suspend fun getGpsLocation()
}