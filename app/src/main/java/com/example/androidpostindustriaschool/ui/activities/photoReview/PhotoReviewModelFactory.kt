package com.example.androidpostindustriaschool.ui.activities.photoReview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository

class PhotoReviewModelFactory(private val repository: FavoritePhotosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotoReviewModel(repository) as T

    }
}