package com.example.androidpostindustriaschool.ui.activities.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.RequestHistory
import com.example.androidpostindustriaschool.data.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    val requestHistory: MutableLiveData<Array<RequestHistory>> = MutableLiveData()


    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHistory()
        }
    }

    fun getHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val allHistory = repository.getAllHistory()
            requestHistory.postValue(allHistory)
        }
    }
}