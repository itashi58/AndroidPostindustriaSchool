package com.example.androidpostindustriaschool

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.data.repository.Repository

class MainViewModelFactory(private val repository: Repository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository, context) as T

    }
}