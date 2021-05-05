package com.example.androidpostindustriaschool.ui.main

import androidx.lifecycle.*
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.repository.MainRepository
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
                when (val response = repository.getFlickrAPIService(search)) {
                    null -> flickrSearchResponse.postValue(R.string.title_no_interet)
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

    fun deleteId(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromDB(id)
        }
    }
}