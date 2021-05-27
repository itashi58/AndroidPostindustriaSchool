package com.example.androidpostindustriaschool.ui.activities.favorites.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository

class FavoritesViewModelFactory(private val repository: FavoritePhotosRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repository) as T

    }
}