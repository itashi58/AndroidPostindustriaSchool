package com.example.androidpostindustriaschool.ui.activities.main

import androidx.lifecycle.*
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.repository.HistoryRepository
import com.example.androidpostindustriaschool.data.repository.PhotoRepository
import com.example.androidpostindustriaschool.util.Constants.Companion.GEOLOCATION_SEARCH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val photoRepository: PhotoRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val flickrSearchResponse: MutableLiveData<List<String>> = MutableLiveData()
    val flickrSearchError: MutableLiveData<Int> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var lastRequest: String

    fun searchInFlickr(search: String) {
        if (search.isEmpty() || search.isBlank()) {
            flickrSearchError.postValue(R.string.msg_search_empty)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                progressBarVisibility.postValue(true)
                val response = photoRepository.getFlickrAPIResponse(search)
                handleFlickrResponse(response, search)
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
            val response = photoRepository.getFlickrAPIResponseLocation(latitude, longitude)
            handleFlickrResponse(response, GEOLOCATION_SEARCH)
            progressBarVisibility.postValue(false)
        }
    }


    private suspend fun handleFlickrResponse(response: ArrayList<String>?, request: String) {
        when {
            response == null -> { //connection or internal problems
                flickrSearchError.postValue(R.string.error_no_internet)
            }
            response.isEmpty() -> { // no data for this request on api
                flickrSearchError.postValue(R.string.msg_no_search_result)
            }
            else -> { //correct response
                lastRequest = request
                flickrSearchResponse.postValue(response)
                photoRepository.deleteAllFromDB()
                photoRepository.insertDB(response, request)
            }
        }
    }


    fun deleteId(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            photoRepository.deleteFromDB(id)
        }
    }

    fun insertInHistory(request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.insertInHistoryDB(request)
        }
    }
}