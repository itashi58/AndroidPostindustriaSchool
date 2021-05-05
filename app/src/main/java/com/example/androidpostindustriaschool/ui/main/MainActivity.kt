package com.example.androidpostindustriaschool.ui.main

import android.content.Context
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
import com.example.androidpostindustriaschool.util.Constants


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var searchButton: Button
    private lateinit var searchInputField: EditText
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var lastRequest: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.searchBtn)
        searchInputField = findViewById(R.id.searchEditText)
        photoRecyclerView = findViewById(R.id.recyclerview)
        progressBar = findViewById(R.id.progressBarMain)
        lastRequest = getSharedPreferences(Constants.LAST_REQUEST, Context.MODE_PRIVATE)

        val repository = MainRepository(DatabaseSQLite.getDatabase(this).photoDao())
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val adapter = PhotoAdapter()
        photoRecyclerView.adapter = adapter
        photoRecyclerView.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.span_count))

        val itemTouchHelper = ItemTouchHelper(SwipeToDelete(adapter, viewModel))
        itemTouchHelper.attachToRecyclerView(photoRecyclerView)

        viewModel.flickrSearchResponse.observe(this, { response ->
            when (response) {
                is ArrayList<*> -> {
                    adapter.updateList(response as ArrayList<String>)
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