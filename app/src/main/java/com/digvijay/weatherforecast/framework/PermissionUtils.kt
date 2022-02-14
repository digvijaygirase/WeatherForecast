package com.digvijay.weatherforecast.framework

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.digvijay.weatherforecast.R
import com.google.android.gms.location.LocationServices

class PermissionUtils {
    companion object {

        const val LOCATION_PERMISSION_REQUEST_CODE = 101

        /**
         * Function to request permission from the user
         */
        fun requestAccessFineLocationPermission(activity: AppCompatActivity, requestId: Int) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestId
            )
        }

        /**
         * Function to check if the location permissions are granted or not
         */
        fun isAccessFineLocationGranted(context: Context): Boolean {
            return ContextCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * Function to check if location of the device is enabled or not
         */
        fun isLocationEnabled(context: Context): Boolean {
            val locationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

        /**
         * Function to show the "enable GPS" Dialog box
         */
        fun showGPSNotEnabledDialog(context: Context) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.enable_gps))
                .setMessage(context.getString(R.string.required_for_this_app))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.enable_now)) { _, _ ->
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .show()
        }

        fun requestPermissions(activity: AppCompatActivity){
            val locationPermissionRequest = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        // Precise location access granted.

                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        // Only approximate location access granted.

                    }
                    else -> {
                        // No location access granted.
                        Toast.makeText(activity, "Location permission not granted", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}