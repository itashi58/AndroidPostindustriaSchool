package com.example.androidpostindustriaschool.ui.main_activity

import androidx.lifecycle.*
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.repository.MainRepository
import com.example.androidpostindustriaschool.util.Constants.Companion.GEOLOCATION_SEARCH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val flickrSearchResponse: MutableLiveData<Any> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var lastRequest: String

    fun searchInFlickr(search: String) {
        if (search.isEmpty()) {
            flickrSearchResponse.postValue(R.string.title_search_empty)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                progressBarVisibility.postValue(true)
                when (val response = repository.getFlickrAPIResponse(search)) {
                    null -> flickrSearchResponse.postValue(R.string.title_no_internet)
                    ArrayList<String>() -> flickrSearchResponse.postValue(R.string.title_no_search_result)
                    else -> {
                        lastRequest = search
                        flickrSearchResponse.postValue(response)
                        repository.deleteAllFromDB()
                        repository.insertDB(response, search)
                    }
                }
                progressBarVisibility.postValue(false)
            }
        }
    }

    fun searchInFlickrLocation(
        latitude: Double,
        longitude: Double
    ) {
            viewModelScope.launch(Dispatchers.IO) {
                progressBarVisibility.postValue(true)
                when (val response = repository.getFlickrAPIResponseLocation(latitude, longitude)) {
                    null -> flickrSearchResponse.postValue(R.string.title_no_internet)
                    ArrayList<String>() -> flickrSearchResponse.postValue(R.string.title_no_search_result)
                    else -> {
                        lastRequest = GEOLOCATION_SEARCH
                        flickrSearchResponse.postValue(response)
                        repository.deleteAllFromDB()
                        repository.insertDB(response, GEOLOCATION_SEARCH)
                    }
                }
                progressBarVisibility.postValue(false)
            }
    }

    fun deleteId(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromDB(id)
        }
    }

    fun insertInHistory(request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertInHistoryDB(request)
        }
    }
}