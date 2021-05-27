package com.example.androidpostindustriaschool.ui.activities.main.view_model

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

    private val _flickrSearchResponse: MutableLiveData<List<String>> = MutableLiveData()
    private val _flickrSearchError: MutableLiveData<Int> = MutableLiveData()
    private val _progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()

    val flickrSearchResponse: LiveData<List<String>>
        get() = _flickrSearchResponse
    val flickrSearchError: LiveData<Int>
        get() = _flickrSearchError
    val progressBarVisibility: LiveData<Boolean>
        get() = _progressBarVisibility

    lateinit var lastRequest: String

    fun searchInFlickr(search: String) {
        if (search.isEmpty() || search.isBlank()) {
            _flickrSearchError.postValue(R.string.msg_search_empty)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _progressBarVisibility.postValue(true)
                val response = photoRepository.getFlickrAPIResponse(search)
                handleFlickrResponse(response, search)
                _progressBarVisibility.postValue(false)
            }
        }
    }


    fun searchInFlickrLocation(
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _progressBarVisibility.postValue(true)
            val response = photoRepository.getFlickrAPIResponseLocation(latitude, longitude)
            handleFlickrResponse(response, GEOLOCATION_SEARCH)
            _progressBarVisibility.postValue(false)
        }
    }


    private suspend fun handleFlickrResponse(response: ArrayList<String>?, request: String) {
        when {
            response == null -> { //connection or internal problems
                _flickrSearchError.postValue(R.string.error_no_internet)
            }
            response.isEmpty() -> { // no data for this request on api
                _flickrSearchError.postValue(R.string.msg_no_search_result)
            }
            else -> { //correct response
                lastRequest = request
                _flickrSearchResponse.postValue(response)
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