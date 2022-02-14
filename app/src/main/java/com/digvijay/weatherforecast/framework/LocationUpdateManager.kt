package com.digvijay.weatherforecast.framework

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class LocationUpdateManager(private val context: Context) {

    fun getUpdatedLocation(callback: (Location?) -> Unit) {
        val permission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            val locationRequest = LocationRequest.create()
                .setInterval(2000)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            callback.invoke(location)
                        }
                    }
                },
                Looper.getMainLooper()
            )
            fusedLocationProviderClient
                .lastLocation.addOnSuccessListener { location ->
                    callback.invoke(location)
                }
        } else {
            callback.invoke(null)
        }

    }
}