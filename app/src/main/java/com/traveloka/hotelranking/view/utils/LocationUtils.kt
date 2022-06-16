package com.traveloka.hotelranking.view.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import timber.log.Timber

class LocationUtils {
    private var currentLocation: Location? = null
    private var isUpdate = true

    fun isGPSEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getLocation(): Location? {
        return currentLocation
    }

    fun getCurrentLocation(context: Context?, listener: OnSuccessListener<Location?>) {
        isUpdate = true
        if (!isPermissionEnabled(context)) {
            requestLocationPermission(context)
            return
        }
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000 // 10 seconds
        locationRequest.fastestInterval = 5000 // 5 seconds
        val locationCallback: LocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.equals(null) ) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null && isUpdate) {
                        listener.onSuccess(location)
                        currentLocation = location
                        isUpdate = false
                        Timber.tag(LocationUtils::class.java.simpleName.toString()).d(
                            "== LocationCallback UPDATE : ${location.latitude} , ${location.longitude}"
                        )
                    }
                }
            }
        }
        val mFusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context!!)
        if (LocationUtils().isPermissionEnabled(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            mFusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    currentLocation = location
                    if (location != null && isUpdate) {
                        listener.onSuccess(location)
                        isUpdate = false

                    }
                }
                .addOnFailureListener {
                    listener.onSuccess(null)
                }
        } else {
            listener.onSuccess(null)
        }
    }


    fun isPermissionEnabled(context: Context?): Boolean {
        val fine =
            ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
        val course =
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED && course == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(context: Context?) {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
    }

}