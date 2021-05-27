package com.example.androidpostindustriaschool.ui.activities.photo_review.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoReviewViewModel(private val repository: FavoritePhotosRepository) : ViewModel() {

    private val _inFavorites: MutableLiveData<Boolean> = MutableLiveData()
    val inFavorites: LiveData<Boolean>
        get() = _inFavorites

    fun insertInChosenPhoto(url: String, request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertInFavoritePhotos(FavoritePhoto(url + request, url, request))
        }
    }

    fun deleteFromChosenPhoto(url: String, request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromFavoritePhotos(url + request)
        }
    }

    // repository.isInFavoritePhotos(id) returns: 1 - photo is favorite, 2 - photo not favorite
    fun idInChosenPhoto(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _inFavorites.postValue(repository.isInFavoritePhotos(id) == 1)
        }
    }
}