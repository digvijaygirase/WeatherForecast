package com.digvijay.weatherforecast.data

import com.digvijay.weatherforecast.domain.Weather

interface PrefsDataSource {

    suspend fun getWeatherData(): Weather
    suspend fun saveWeatherData(weather: Weather)
}