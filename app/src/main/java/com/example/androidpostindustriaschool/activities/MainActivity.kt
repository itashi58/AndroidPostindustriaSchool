package com.example.androidpostindustriaschool.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.MainViewModel
import com.example.androidpostindustriaschool.MainViewModelFactory
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.repository.Repository
import com.example.androidpostindustriaschool.util.Constants.Companion.RESPONSE_TEXTVIEW_KEY
import com.example.androidpostindustriaschool.util.Constants.Companion.SEARCH_FIELD_KEY
import org.bluecabin.textoo.Textoo

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var searchButton: Button
    private lateinit var searchInputField: EditText
    private lateinit var apiResponseTextView: TextView
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.searchBtn)
        searchInputField = findViewById(R.id.searchEditText)
        apiResponseTextView = findViewById(R.id.apiResponseTextView)
        progressBar = findViewById(R.id.progressBarMain)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository, this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.flickrSearchResponse.observe(this, { response ->
            apiResponseTextView.text = response
            linkify()
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
        outState.putCharSequence(RESPONSE_TEXTVIEW_KEY, apiResponseTextView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.searchEditText).text =
                savedInstanceState.getCharSequence(SEARCH_FIELD_KEY) as Editable?
        findViewById<TextView>(R.id.apiResponseTextView).text =
                savedInstanceState.getCharSequence(RESPONSE_TEXTVIEW_KEY)
        linkify()
    }

    //controls progress bar visibility
    private fun progressBarVisible(boolean: Boolean) {
        if (boolean) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    //in apiResponseTextView make links with intents leading to webViewActivity
    private fun linkify() {
        apiResponseTextView = Textoo
                .config(apiResponseTextView)
                .linkifyAll() // or just .linkifyAll()
                .addLinksHandler { view, url ->
                    val intent = Intent(this, WebViewActivity::class.java).apply {
                        data = Uri.parse(url)
                    }
                    startActivity(intent)
                    true
                }
                .apply()
    }
}