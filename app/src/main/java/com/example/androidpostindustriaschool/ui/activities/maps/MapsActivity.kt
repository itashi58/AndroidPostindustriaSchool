package com.example.androidpostindustriaschool.ui.activities.maps


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.databinding.ActivityMapsBinding
import com.example.androidpostindustriaschool.ui.activities.main.view.MainActivity
import com.example.androidpostindustriaschool.util.Constants.Companion.KIEV_LATITUDE
import com.example.androidpostindustriaschool.util.Constants.Companion.KIEV_LONGITUDE
import com.example.androidpostindustriaschool.util.Constants.Companion.LATITUDE_EXTRA
import com.example.androidpostindustriaschool.util.Constants.Companion.LONGITUDE_EXTRA
import com.example.androidpostindustriaschool.util.Constants.Companion.PERMISSION_LOCATION_CODE
import com.example.androidpostindustriaschool.util.Constants.Companion.MAP_ZOOM
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsLocationProvider:MapsLocationProvider
    private var latitude = 50.450001
    private var longitude = 30.523333


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.f_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsLocationProvider =  MapsLocationProvider(getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.btnSearchInLocation.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(LATITUDE_EXTRA, latitude)
            intent.putExtra(LONGITUDE_EXTRA, longitude)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun setObservers() {
        mapsLocationProvider.currentLocation.observe(this) { currentLocation ->
            mapAddNewMarker(currentLocation)
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentLocation,
                    MAP_ZOOM
                )
            )
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
        this.googleMap = googleMap
        this.googleMap.uiSettings.isZoomControlsEnabled = true

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

        this.googleMap.setOnMapClickListener { location ->
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

        val lastLocation = mapsLocationProvider.getLastLocation()
        when {
            lastLocation != null -> {
                lastLocation.latitude
                val lastLocationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                mapAddNewMarker(lastLocationLatLng)
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        lastLocationLatLng,
                        MAP_ZOOM
                    )
                )

            }

            mapsLocationProvider.isProviderGPSEnabled() -> {
                createToast(R.string.msg_wait_for_location)
                mapsLocationProvider.startLocationSearch()
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
        val kievLocation = LatLng(KIEV_LATITUDE, KIEV_LONGITUDE)
        mapAddNewMarker(kievLocation)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kievLocation, MAP_ZOOM))
        createToast(messageResId)
    }

    private fun mapAddNewMarker(markerLocation: LatLng) {
        googleMap.clear()
        googleMap.addMarker(
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