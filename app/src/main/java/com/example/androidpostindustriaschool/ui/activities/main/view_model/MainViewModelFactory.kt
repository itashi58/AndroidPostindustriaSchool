package com.example.androidpostindustriaschool.ui.activities.main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.data.repository.HistoryRepository
import com.example.androidpostindustriaschool.data.repository.PhotoRepository

class MainViewModelFactory(
    private val photoRepository: PhotoRepository,
    private val historyRepository: HistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(photoRepository, historyRepository) as T

    }
}