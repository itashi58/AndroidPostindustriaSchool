package com.example.androidpostindustriaschool.ui.maps_activity


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
import com.example.androidpostindustriaschool.ui.main_activity.MainActivity
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

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var searchBtn: Button
    private lateinit var locationManager: LocationManager
    private val kievLocation = LatLng(50.450001, 30.523333)
    private val mapZoom = 10f
    private var latitude = 50.450001
    private var longitude = 30.523333


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchBtn = findViewById(R.id.searchInLocationBtn)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
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
                    locationPermissionNotGranted()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun locationPermissionGranted() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        val providerName = locationManager.getBestProvider(criteria, true)

        if (providerName != null) {
            val lastLocation = locationManager.getLastKnownLocation(providerName)
            if (lastLocation != null) {
                val lastLocationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                mapAddNewMarker(lastLocationLatLng)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationLatLng, mapZoom))
            } else {
                mapAddNewMarker(kievLocation)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kievLocation, mapZoom))
                val toast = Toast.makeText(
                    this,
                    getString(R.string.title_no_last_location),
                    Toast.LENGTH_LONG
                )
                toast.show()
            }
        } else {
            mapAddNewMarker(kievLocation)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kievLocation, mapZoom))
            val toast = Toast.makeText(
                this,
                getString(R.string.title_turn_on_location),
                Toast.LENGTH_LONG
            )
            toast.show()
        }

        mMap.setOnMapClickListener { location ->
            mapAddNewMarker(location)
        }
    }

    private fun locationPermissionNotGranted() {
        mapAddNewMarker(kievLocation)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kievLocation, mapZoom))
        val toast = Toast.makeText(
            this,
            getString(R.string.title_no_last_location),
            Toast.LENGTH_LONG
        )
        toast.show()
    }

    private fun mapAddNewMarker(markerLocation: LatLng) {
        mMap.clear()
        mMap.addMarker(
            MarkerOptions().position(markerLocation).title("Current location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )?.isDraggable = true
        latitude = markerLocation.latitude
        longitude = markerLocation.longitude
    }
}