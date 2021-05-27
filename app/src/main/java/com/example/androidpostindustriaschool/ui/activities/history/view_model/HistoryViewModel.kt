package com.example.androidpostindustriaschool.ui.activities.history.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    private val _requestHistory: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val requestHistory: LiveData<ArrayList<String>>
        get() = _requestHistory



    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHistory()
        }
    }

    fun getHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val history = ArrayList<String>()
            repository.getAllHistory().forEach { entry ->
                history.add(entry.request)
            }
            _requestHistory.postValue(history)
        }
    }
}