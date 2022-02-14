package com.digvijay.weatherforecast.framework

import android.content.Context
import com.digvijay.weatherforecast.data.PrefsDataSource
import com.digvijay.weatherforecast.domain.Weather

class PrefsDataSourceImpl(private val context: Context) : PrefsDataSource {

    private val SHARED_PREF_NAME = "shared_pref"

    override suspend fun getWeatherData(): Weather {
        val sharedPref = context.getSharedPreferences(
            SHARED_PREF_NAME, Context.MODE_PRIVATE
        )
        return Weather(
            sharedPref.getString("location", "")!!,
            sharedPref.getString("temperature", "")!!,
            sharedPref.getString("weatherDescription", "")!!
        )
    }

    override suspend fun saveWeatherData(weather: Weather) {
        val sharedPref =
            context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("location", weather.location)
            putString("temperature", weather.temperature)
            putString("weatherDescription", weather.weatherDescription)
            apply()
        }
    }
}