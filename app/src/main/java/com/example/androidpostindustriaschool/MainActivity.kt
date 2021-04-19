package com.example.androidpostindustriaschool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.data.repository.Repository

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var searchButton: Button
    lateinit var searchInputField: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchButton = findViewById<Button>(R.id.button)
        searchInputField = findViewById(R.id.editText)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)



        searchButton.setOnClickListener {
            val searchRequest = searchInputField.text.toString()
            Log.d("request", searchRequest)
            viewModel.getPost(searchRequest)
            viewModel.myResponse.observe(this, Observer { response ->
                Log.d("Response", response.photos.photo[0].owner)
            })
        }
    }
}