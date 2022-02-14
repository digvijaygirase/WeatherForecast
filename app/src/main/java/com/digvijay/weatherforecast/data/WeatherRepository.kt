package com.digvijay.weatherforecast.data

import com.digvijay.weatherforecast.domain.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val apiDataSource: ApiDataSource,
    private val prefsDataSource: PrefsDataSource
) {

    suspend fun getWeatherData(): Weather {
        if(prefsDataSource.getWeatherData().location.isEmpty()){
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                prefsDataSource.saveWeatherData(apiDataSource.getWeatherData())
            }
        }
        return prefsDataSource.getWeatherData()
    }

    suspend fun syncWeatherData(){
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            prefsDataSource.saveWeatherData(apiDataSource.getWeatherData())
        }
    }
}