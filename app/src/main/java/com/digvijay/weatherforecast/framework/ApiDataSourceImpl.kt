package com.digvijay.weatherforecast.framework

import android.location.Location
import com.digvijay.weatherforecast.data.ApiDataSource
import com.digvijay.weatherforecast.domain.Weather
import com.digvijay.weatherforecast.framework.api.ApiService

class ApiDataSourceImpl(private val apiService: ApiService, private val location: Location) :
    ApiDataSource {

    override suspend fun getWeatherData(): Weather {
        val response =
            apiService.getWeatherData(location.latitude, location.longitude)
        if (response.isSuccessful) {
            response.body()?.run {
                return Weather(name, main.temp.toString(), weather.firstOrNull()?.description ?: "")
            }
        }
        return Weather("", "", "")
    }
}