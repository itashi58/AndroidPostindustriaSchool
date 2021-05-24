package com.example.androidpostindustriaschool.ui.activities.maps


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.databinding.ActivityMapsBinding
import com.example.androidpostindustriaschool.ui.activities.main.MainActivity
import com.example.androidpostindustriaschool.util.Constants.Companion.LATITUDE_EXTRA
import com.example.androidpostindustriaschool.util.Constants.Companion.LONGITUDE_EXTRA
import com.example.androidpostindustriaschool.util.Constants.Companion.PERMISSION_LOCATION_CODE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    // TODO: 24.05.2021 Avoid `m` prefix
    private lateinit var mMap: GoogleMap

    // TODO: 24.05.2021 Why this is defined and not used? Read about viewBinding.
    private lateinit var binding: ActivityMapsBinding

    // TODO: 24.05.2021 you can access this view from `binding` instance
    private lateinit var searchBtn: Button

    // TODO: 24.05.2021 Work with locaitonManager should be encapsulated in some LocationProvider
    private lateinit var locationManager: LocationManager
    private val locManMinTimeMs: Long = 1000
    private val locManMinDistance: Float = 10f
    private val kievLocation = LatLng(50.450001, 30.523333) // TODO: 24.05.2021 should be constant
    private val mapZoom = 10f // TODO: 24.05.2021 should be constant
    private var latitude = 50.450001
    private var longitude = 30.523333


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchBtn = findViewById(R.id.btn_search_in_location)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.f_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setListeners()
    }

    private fun setListeners() {
        searchBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(LATITUDE_EXTRA, latitude)
            intent.putExtra(LONGITUDE_EXTRA, longitude)
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION_CODE
            )
        }

        mMap.setOnMapClickListener { location ->
            mapAddNewMarker(location)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_LOCATION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    locationPermissionGranted()
                } else {
                    locationPermissionProblem(R.string.error_no_location_permission)
                }
                return
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun locationPermissionGranted() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        when {
            lastLocation != null -> {
                lastLocation.latitude
                val lastLocationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                mapAddNewMarker(lastLocationLatLng)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationLatLng, mapZoom))

            }

            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                createToast(R.string.msg_wait_for_location)
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    locManMinTimeMs,
                    locManMinDistance, object : LocationListener {

                        override fun onLocationChanged(p0: Location) {
                            val locationLatLng =
                                LatLng(p0.latitude, p0.longitude)
                            mapAddNewMarker(locationLatLng)
                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    locationLatLng,
                                    mapZoom
                                )
                            )
                            locationManager.removeUpdates(this)
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
            else -> {
                locationPermissionProblem(R.string.error_turn_on_location)
            }
        }
    }


    /**
     * some problem occurred, messageResId will be shown in Toast to user and marker set to Kiev
     */
    private fun locationPermissionProblem(messageResId: Int) {
        mapAddNewMarker(kievLocation)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kievLocation, mapZoom))
        createToast(messageResId)
    }

    private fun mapAddNewMarker(markerLocation: LatLng) {
        mMap.clear()
        mMap.addMarker(
            MarkerOptions().position(markerLocation)
                .title(getString(R.string.title_current_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )?.isDraggable = true
        latitude = markerLocation.latitude
        longitude = markerLocation.longitude
    }

    private fun createToast(messageResId: Int) {
        val toast = Toast.makeText(
            this,
            getString(messageResId),
            Toast.LENGTH_LONG
        )
        toast.show()
    }

}