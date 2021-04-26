package com.example.androidpostindustriaschool

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository, @SuppressLint("StaticFieldLeak") private val context: Context) : ViewModel() {

    val flickrSearchResponse: MutableLiveData<String> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()

    fun searchInFlickr(search: String) {
        if (search.isEmpty()) {
            flickrSearchResponse.postValue(context.getString(R.string.search_empty))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                progressBarVisibility.postValue(true)
                when (val response = repository.getFlickrAPIService(search)) {
                    null -> flickrSearchResponse.postValue(context.getString(R.string.no_interet))
                    "" -> flickrSearchResponse.postValue(context.getString(R.string.no_search_result))
                    else -> flickrSearchResponse.postValue(response)
                }
                progressBarVisibility.postValue(false)
            }
        }
    }
}