package com.example.androidpostindustriaschool

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    val flickrSearchResponse: MutableLiveData<Any> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()

    fun searchInFlickr(search: String) {
        if (search.isEmpty()) {
            flickrSearchResponse.postValue(R.string.search_empty)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                progressBarVisibility.postValue(true)
                when (val response = repository.getFlickrAPIService(search)) {
                    null -> flickrSearchResponse.postValue(R.string.no_interet)
                    ArrayList<String>() -> flickrSearchResponse.postValue(R.string.no_search_result)
                    else -> flickrSearchResponse.postValue(response)
                }
                progressBarVisibility.postValue(false)
            }
        }
    }
}