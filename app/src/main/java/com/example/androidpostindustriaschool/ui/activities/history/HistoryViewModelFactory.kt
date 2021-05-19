package com.example.androidpostindustriaschool.ui.activities.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.data.repository.HistoryRepository

class HistoryViewModelFactory(private val repository: HistoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HistoryViewModel(repository) as T

    }
}