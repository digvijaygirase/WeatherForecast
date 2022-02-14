package com.digvijay.weatherforecast.data

import com.digvijay.weatherforecast.domain.Weather

interface ApiDataSource {

    suspend fun getWeatherData(): Weather
}