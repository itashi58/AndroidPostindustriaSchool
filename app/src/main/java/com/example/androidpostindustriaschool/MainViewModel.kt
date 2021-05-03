package com.example.androidpostindustriaschool

import androidx.lifecycle.*
import com.example.androidpostindustriaschool.data.modeldb.Photo
import com.example.androidpostindustriaschool.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    val flickrSearchResponse: MutableLiveData<Any> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val allPhotos: LiveData<List<Photo>> = MutableLiveData()

    fun searchInFlickr(search: String) {
        if (search.isEmpty()) {
            flickrSearchResponse.postValue(R.string.search_empty)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                progressBarVisibility.postValue(true)
                when (val response = repository.getFlickrAPIService(search)) {
                    null -> flickrSearchResponse.postValue(R.string.no_interet)
                    ArrayList<String>() -> flickrSearchResponse.postValue(R.string.no_search_result)
                    else -> {
                        flickrSearchResponse.postValue(response)
                        repository.deleteAllFromDB()
                        repository.insertDB(response, search)
                    }
                }
                progressBarVisibility.postValue(false)
            }
        }
    }

    fun deleteId(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromDB(id)
        }
    }
}