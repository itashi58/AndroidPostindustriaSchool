package com.example.androidpostindustriaschool.ui.main_activity

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
import com.example.androidpostindustriaschool.data.repository.MainRepository
import com.example.androidpostindustriaschool.ui.favorites_activity.FavoritesActivity
import com.example.androidpostindustriaschool.ui.history_activity.HistoryActivity
import com.example.androidpostindustriaschool.ui.maps_activity.MapsActivity
import com.example.androidpostindustriaschool.util.Constants
import com.example.androidpostindustriaschool.util.Constants.Companion.GEOLOCATION_SEARCH
import com.example.androidpostindustriaschool.util.Constants.Companion.LATITUDE_EXTRA
import com.example.androidpostindustriaschool.util.Constants.Companion.LONGITUDE_EXTRA
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.searchBtn)
        searchInputField = findViewById(R.id.searchEditText)
        photoRecyclerView = findViewById(R.id.recyclerview)
        progressBar = findViewById(R.id.progressBarMain)
        favoritesFab = findViewById(R.id.FavoritesFAB)
        historyButton = findViewById(R.id.historyBtn)
        mapsButton = findViewById(R.id.mapFAB)
        lastRequest = getSharedPreferences(Constants.LAST_REQUEST, Context.MODE_PRIVATE)


        val repository = MainRepository(
            DatabaseSQLite.getDatabase(this).photoDao(),
            DatabaseSQLite.getDatabase(this).requestHistoryDao()
        )
        val viewModelFactory = MainViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val adapter = MainPhotoAdapter()
        photoRecyclerView.adapter = adapter
        photoRecyclerView.layoutManager =
            GridLayoutManager(this, resources.getInteger(R.integer.span_count))

        val itemTouchHelper = ItemTouchHelper(MainSwipeToDelete(adapter, viewModel))
        itemTouchHelper.attachToRecyclerView(photoRecyclerView)

        viewModel.flickrSearchResponse.observe(this, { response ->
            when (response) {
                is ArrayList<*> -> {
                    adapter.updateList(
                        response as ArrayList<String>,
                        searchInputField.text.toString()
                    )
                    viewModel.insertInHistory(searchInputField.text.toString())
                }
                is Int -> {
                    val toast = Toast.makeText(this, getString(response), Toast.LENGTH_LONG)
                    toast.show()
                }
                else -> {
                    val toast = Toast.makeText(
                        this,
                        getString(R.string.title_internal_error),
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    Log.d("MainAct Response error", "Unexpected response type")
                }
            }
        })

        viewModel.progressBarVisibility.observe(this, { visibility ->
            progressBarVisible(visibility)
        })

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
            startActivity(Intent(this, MapsActivity::class.java))
        }

        if (intent.hasExtra(LATITUDE_EXTRA) && intent.hasExtra(LONGITUDE_EXTRA)) {
            searchInputField.setText(GEOLOCATION_SEARCH)
            Log.d("search",searchInputField.text.toString())
            viewModel.searchInFlickrLocation(
                intent.getDoubleExtra(LATITUDE_EXTRA, 0.0),
                intent.getDoubleExtra(LONGITUDE_EXTRA, 0.0)
            )
        }
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

    //controls progress bar visibility
    private fun progressBarVisible(boolean: Boolean) {
        if (boolean) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

}