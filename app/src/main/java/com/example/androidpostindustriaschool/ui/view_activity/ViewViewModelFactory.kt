package com.example.androidpostindustriaschool.ui.view_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.data.repository.ViewRepository

class ViewViewModelFactory(private val repository: ViewRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewViewModel(repository) as T

    }
}