package com.digvijay.weatherforecast.framework

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.*
import com.digvijay.weatherforecast.data.LocationRepository
import com.digvijay.weatherforecast.data.WeatherRepository
import com.digvijay.weatherforecast.framework.api.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LocationWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private lateinit var weatherRepository: WeatherRepository

    private val locationRepository by lazy {
        val locationDataSource =
            LocationDataSourceImpl(LocationUpdateManager(context), ::onLocationReceive)
        LocationRepository(locationDataSource)
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                locationRepository.getDeviceLocation()
                Result.success()
            } catch (e: Exception) {
                Log.d(TAG, "Exception getting location -->  ${e.message}")
                Result.failure()
            }
        }
    }

    private fun onLocationReceive(location: Location?) {
        location?.let {
            val apiDataSource = ApiDataSourceImpl(ApiService(), it)
            weatherRepository = WeatherRepository(apiDataSource, PrefsDataSourceImpl(context))
            fetchWeatherData()
        }
    }

    private fun fetchWeatherData() {
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepository.syncWeatherData()
        }
    }

    companion object {
        private const val TAG = "UpdateLocationWorker"
        private const val MIN_INTERVAL_HRS = 2L

        @JvmStatic
        fun schedule(context: Application) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build()
            val worker =
                PeriodicWorkRequestBuilder<LocationWorker>(MIN_INTERVAL_HRS, TimeUnit.HOURS)
                    .addTag(TAG)
                    .setConstraints(constraints)
                    .build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, worker)
        }
    }
}