package com.example.androidpostindustriaschool.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.MainViewModel
import com.example.androidpostindustriaschool.MainViewModelFactory
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.repository.Repository
import org.bluecabin.textoo.Textoo


// TODO: 4/22/21 The app will crash if running search with empty field
// TODO: 4/22/21 The app will crash if running search without internet. You should add error handling
// TODO: 4/22/21 App doesn't handle empty result
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
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        searchButton.setOnClickListener {

            // TODO: 4/22/21 You should control your progress indicator visibility from view model by providing a progress live data
            progressBar.visibility = View.VISIBLE
            val searchRequest = searchInputField.text.toString()
            viewModel.getPost(searchRequest)

            var answer = ""

            // TODO: 4/22/21 Move your observers outside of onClickListener method You are adding observers each time you click a button.
            //  This is why you are skipped frames issue and progress dialog issues
            viewModel.myResponse.observe(this, Observer { response ->
                // TODO: 4/22/21 Better to perform this operation outside of presentation layer
                response.photos.photo.forEach {
                    answer += "https://farm" + it.farm + ".staticflickr.com/" + it.server + "/" + it.id + "_" + it.secret + ".jpg" + "\n"
                }
                Log.d("link", answer)
                progressBar.visibility = View.INVISIBLE
                apiResponseTextView.text = answer
                linkify()
            })
        }


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // TODO: 4/22/21 Avoid hardcode. Move this into constants. Refer to review from first week.
        outState.putCharSequence("searchInputField", searchInputField.text)
        outState.putCharSequence("apiResponseTextView", apiResponseTextView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.searchEditText).text =
            savedInstanceState.getCharSequence("searchInputField") as Editable?
        findViewById<TextView>(R.id.apiResponseTextView).text =
            savedInstanceState.getCharSequence("apiResponseTextView")
        linkify()
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