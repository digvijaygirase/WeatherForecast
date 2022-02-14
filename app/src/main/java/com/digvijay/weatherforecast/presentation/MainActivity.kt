package com.digvijay.weatherforecast.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.digvijay.weatherforecast.R
import com.digvijay.weatherforecast.databinding.ActivityMainBinding
import com.digvijay.weatherforecast.framework.LocationWorker
import com.digvijay.weatherforecast.framework.PermissionUtils
import com.digvijay.weatherforecast.framework.ViewModelFactory
import com.digvijay.weatherforecast.framework.api.Status

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainViewModel = ViewModelFactory(application).create(MainViewModel::class.java)

        mainViewModel.resourceLiveData.observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        mainBinding.progressBar.visibility = View.GONE
                        mainBinding.displayViewsGroup.visibility = View.VISIBLE
                        resource.data?.let { weather ->
                            mainBinding.locationTxv.text =
                                getString(R.string.location, weather.location)
                            mainBinding.temperatureTxv.text =
                                getString(R.string.temperature, weather.temperature)
                            mainBinding.weatherTxv.text =
                                getString(R.string.weather, weather.weatherDescription)
                        }
                    }
                    Status.ERROR -> {
                        mainBinding.progressBar.visibility = View.GONE
                        mainBinding.displayViewsGroup.visibility = View.INVISIBLE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        mainBinding.progressBar.visibility = View.VISIBLE
                        mainBinding.displayViewsGroup.visibility = View.INVISIBLE
                    }
                }
            }
        })

        checkLocationPermissions()
        LocationWorker.schedule(application)
    }

    private fun checkLocationPermissions() {
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) ->
                        mainViewModel.fetchLocation()
                    else -> PermissionUtils.showGPSNotEnabledDialog(this)
                }
            }
            else -> PermissionUtils.requestAccessFineLocationPermission(
                this,
                PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // Location Permission
            PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            mainViewModel.fetchLocation()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.perms_not_granted), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}