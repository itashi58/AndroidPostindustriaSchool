package com.example.androidpostindustriaschool.ui.main_activity

import androidx.lifecycle.*
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    // TODO: 5/12/21 Add a separate liveData for errors. Don't mix valid response handling and error handling as it is hard to understand and support.
    val flickrSearchResponse: MutableLiveData<Any> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var lastRequest: String

    fun searchInFlickr(search: String) {
        if (search.isEmpty()) {
            flickrSearchResponse.postValue(R.string.title_search_empty)
        } else {
            // TODO: 5/12/21 nesting is messy. Separate logic into separate methods + add appropriate error handling
            viewModelScope.launch(Dispatchers.IO) {
                progressBarVisibility.postValue(true)
                when (val response = repository.getFlickrAPIService(search)) {
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