package com.example.androidpostindustriaschool.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.MainViewModel
import com.example.androidpostindustriaschool.MainViewModelFactory
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.PhotoDatabase
import com.example.androidpostindustriaschool.data.repository.Repository
import com.example.androidpostindustriaschool.util.Constants.Companion.SEARCH_FIELD_KEY


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var searchButton: Button
    private lateinit var searchInputField: EditText
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.searchBtn)
        searchInputField = findViewById(R.id.searchEditText)
        photoRecyclerView = findViewById(R.id.recyclerview)
        progressBar = findViewById(R.id.progressBarMain)

        val repository = Repository(PhotoDatabase.getDatabase(this).photoDao())
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.flickrSearchResponse.observe(this, { response ->
            when (response) {
                is ArrayList<*> -> {
                    val adapter = PhotoAdapter(response as ArrayList<String>)
                    photoRecyclerView.layoutManager = GridLayoutManager(this, 1)
                    photoRecyclerView.adapter = adapter

                    val itemTouchHelper = ItemTouchHelper(SwipeToDelete(adapter, viewModel))
                    itemTouchHelper.attachToRecyclerView(photoRecyclerView)
                }
                is Int -> {
                    val toast = Toast.makeText(this, getString(response), Toast.LENGTH_LONG)
                    toast.show()
                }
                else -> {
                    val toast = Toast.makeText(this, getString(R.string.internal_error), Toast.LENGTH_LONG)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_FIELD_KEY, searchInputField.text)
//        outState.putCharSequence(RESPONSE_TEXTVIEW_KEY, apiResponseTextView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.searchEditText).text =
                savedInstanceState.getCharSequence(SEARCH_FIELD_KEY) as Editable?
//        findViewById<TextView>(R.id.apiResponseTextView).text =
//                savedInstanceState.getCharSequence(RESPONSE_TEXTVIEW_KEY)
//        linkify()
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