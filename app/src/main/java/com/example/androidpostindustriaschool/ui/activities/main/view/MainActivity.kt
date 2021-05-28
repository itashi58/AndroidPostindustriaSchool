package com.example.androidpostindustriaschool.ui.activities.main.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.HistoryRepository
import com.example.androidpostindustriaschool.data.repository.PhotoRepository
import com.example.androidpostindustriaschool.ui.activities.favorites.view.FavoritesActivity
import com.example.androidpostindustriaschool.ui.activities.gallery.view.GalleryActivity
import com.example.androidpostindustriaschool.ui.activities.history.view.HistoryActivity
import com.example.androidpostindustriaschool.ui.activities.main.view_model.MainViewModel
import com.example.androidpostindustriaschool.ui.activities.main.view_model.MainViewModelFactory
import com.example.androidpostindustriaschool.ui.activities.maps.MapsActivity
import com.example.androidpostindustriaschool.util.Constants
import com.example.androidpostindustriaschool.util.Constants.Companion.GEOLOCATION_SEARCH
import com.example.androidpostindustriaschool.util.Constants.Companion.LATITUDE_EXTRA
import com.example.androidpostindustriaschool.util.Constants.Companion.LONGITUDE_EXTRA
import com.example.androidpostindustriaschool.util.Constants.Companion.MAPS_REQUEST_CODE
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var searchButton: Button
    private lateinit var searchInputField: EditText
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var lastRequest: SharedPreferences
    private lateinit var favoritesFab: FloatingActionButton
    private lateinit var historyButton: ImageButton
    private lateinit var mapsButton: ImageButton
    private lateinit var galleryButton: ImageButton
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainPhotoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        lastRequest = getSharedPreferences(Constants.LAST_REQUEST, Context.MODE_PRIVATE)

        val photoRepository = PhotoRepository(
            DatabaseSQLite.getDatabase(this).photoDao()
        )
        val historyRepository = HistoryRepository(
            DatabaseSQLite.getDatabase(this).requestHistoryDao()
        )
        val viewModelFactory = MainViewModelFactory(photoRepository, historyRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        initRecyclerView()
        setObservers()
        setListeners()
    }

    override fun onPause() {
        super.onPause()
        val editor = lastRequest.edit()
        editor.putString(Constants.LAST_REQUEST, searchInputField.text.toString()).apply()
    }

    override fun onResume() {
        super.onResume()
        if (lastRequest.contains(Constants.LAST_REQUEST)) {
            searchInputField.setText(lastRequest.getString(Constants.LAST_REQUEST, ""))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("stops", "onResult")
        if (requestCode == MAPS_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(LATITUDE_EXTRA) && data.hasExtra(LONGITUDE_EXTRA)) {
                    //setting lastRequest as GEOLOCATION_SEARCH to be shown in search field and in history
                    val editor = lastRequest.edit()
                    editor.putString(Constants.LAST_REQUEST, GEOLOCATION_SEARCH).apply()

                    viewModel.searchInFlickrLocation(
                        data.getDoubleExtra(LATITUDE_EXTRA, 0.0),
                        data.getDoubleExtra(LONGITUDE_EXTRA, 0.0)
                    )
                }
            }
        }
    }


    private fun initViews() {
        searchButton = findViewById(R.id.btn_search)
        searchInputField = findViewById(R.id.et_search)
        photoRecyclerView = findViewById(R.id.rv_photo)
        progressBar = findViewById(R.id.pb_main)
        favoritesFab = findViewById(R.id.fab_favorites)
        historyButton = findViewById(R.id.btn_history)
        mapsButton = findViewById(R.id.fab_map)
        galleryButton = findViewById(R.id.fab_gallery)
    }

    private fun setListeners() {
        searchButton.setOnClickListener {
            val searchRequest = searchInputField.text.toString()
            viewModel.searchInFlickr(searchRequest)
        }

        historyButton.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        favoritesFab.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        mapsButton.setOnClickListener {
            startActivityForResult(Intent(this, MapsActivity::class.java), MAPS_REQUEST_CODE)
        }

        galleryButton.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }
    }

    private fun setObservers() {
        viewModel.flickrSearchResponse.observe(this, { response ->
            adapter.updateList(
                response,
                searchInputField.text.toString()
            )
            viewModel.insertInHistory(searchInputField.text.toString())
        })

        viewModel.flickrSearchError.observe(this, { errorResourceCode ->
            val toast = Toast.makeText(this, getString(errorResourceCode), Toast.LENGTH_LONG)
            toast.show()
        })

        viewModel.progressBarVisibility.observe(this, { visibility ->
            progressBarVisible(visibility)
            Log.d("progressBarChange", visibility.toString())
        })

        adapter.deletePhoto.observe(this, { deletePhotoUrl ->
            viewModel.deleteId(deletePhotoUrl + viewModel.lastRequest) //id = url+request
        })
    }

    private fun initRecyclerView() {
        adapter = MainPhotoAdapter()
        photoRecyclerView.adapter = adapter
        photoRecyclerView.layoutManager =
            GridLayoutManager(this, resources.getInteger(R.integer.span_count))

        val itemTouchHelper = ItemTouchHelper(MainSwipeToDelete())
        itemTouchHelper.attachToRecyclerView(photoRecyclerView)
    }

    //controls progress bar visibility
    private fun progressBarVisible(boolean: Boolean) {
        if (boolean) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

}