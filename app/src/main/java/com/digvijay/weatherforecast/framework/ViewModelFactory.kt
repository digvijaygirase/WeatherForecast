package com.digvijay.weatherforecast.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.digvijay.weatherforecast.presentation.MainViewModel

class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(application) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }

}