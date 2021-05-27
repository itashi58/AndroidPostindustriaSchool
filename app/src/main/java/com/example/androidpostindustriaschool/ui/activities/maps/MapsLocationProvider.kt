package com.example.androidpostindustriaschool.ui.activities.maps

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng

@SuppressLint("MissingPermission")
class MapsLocationProvider(private val locationManager: LocationManager) {

    private val locManMinTimeMs: Long = 1000
    private val locManMinDistance: Float = 10f
    private val _currentLocation: MutableLiveData<LatLng> = MutableLiveData()
    val currentLocation: LiveData<LatLng>
        get() = _currentLocation


    fun getLastLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }

    fun isProviderGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun startLocationSearch() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            locManMinTimeMs,
            locManMinDistance, object : LocationListener {

                override fun onLocationChanged(p0: Location) {
                    val locationLatLng =
                        LatLng(p0.latitude, p0.longitude)
                    locationManager.removeUpdates(this)
                    _currentLocation.postValue(locationLatLng)
                }

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}

                override fun onStatusChanged(
                    provider: String?,
                    status: Int,
                    extras: Bundle?
                ) {
                }
            }
        )
    }
}