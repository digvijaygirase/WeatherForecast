package com.digvijay.weatherforecast.framework

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class WeatherApp : Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(LocationWorkerFactory())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this, workManagerConfiguration)
    }
}