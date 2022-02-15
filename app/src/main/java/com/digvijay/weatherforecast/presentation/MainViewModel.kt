package com.digvijay.weatherforecast.presentation

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.digvijay.weatherforecast.data.LocationRepository
import com.digvijay.weatherforecast.data.WeatherRepository
import com.digvijay.weatherforecast.domain.Weather
import com.digvijay.weatherforecast.framework.ApiDataSourceImpl
import com.digvijay.weatherforecast.framework.LocationDataSourceImpl
import com.digvijay.weatherforecast.framework.LocationUpdateManager
import com.digvijay.weatherforecast.framework.PrefsDataSourceImpl
import com.digvijay.weatherforecast.framework.api.ApiService
import com.digvijay.weatherforecast.framework.api.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val resourceLiveData = MutableLiveData<Resource<Weather>>()

    private lateinit var weatherRepository: WeatherRepository

    private val locationRepository by lazy {
        val locationDataSource =
            LocationDataSourceImpl(LocationUpdateManager(getApplication()), ::onLocationReceive)
        LocationRepository(locationDataSource)
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            try {
                resourceLiveData.postValue(Resource.success(weatherRepository.getWeatherData()))
            } catch (exception: Exception) {
                resourceLiveData.postValue(Resource.error(null, "Something went wrong."))
            }
        }
    }

    fun fetchLocation() {
        resourceLiveData.value = Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO) { locationRepository.getDeviceLocation() }
    }

    private fun onLocationReceive(location: Location?) {
        location?.let {
            val apiDataSource = ApiDataSourceImpl(ApiService(), it)
            weatherRepository =
                WeatherRepository(apiDataSource, PrefsDataSourceImpl(getApplication()))
            fetchWeatherData()
        }
    }
}