package com.example.androidpostindustriaschool.ui.view_activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.ChosenPhoto
import com.example.androidpostindustriaschool.data.repository.ViewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ViewViewModel(private val repository: ViewRepository) : ViewModel() {

    val inFavorites: MutableLiveData<Boolean> = MutableLiveData()

    fun insertInChosenPhoto(url: String, request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertInChosenPhotoDB(ChosenPhoto(url + request, url, request))
        }
    }

    fun deleteFromChosenPhoto(url: String, request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromChosenPhotoDB(url+request)
        }
    }

    fun isInChosenPhoto(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.isInChosenPhotoDB(id)) {
                1 -> inFavorites.postValue(true)
                0 -> inFavorites.postValue(false)
                else -> {
                    Log.d("DB mistake", "incorect respone")
                    inFavorites.postValue(false)
                }
            }
        }
    }
}