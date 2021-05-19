package com.example.androidpostindustriaschool.ui.activities.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritesViewModel(private val repository: FavoritePhotosRepository) : ViewModel() {
    val favoritePhotos: MutableLiveData<Map<String, List<FavoritePhoto>>> = MutableLiveData()
    val noFavoritePhotos: MutableLiveData<Boolean> = MutableLiveData()

    fun getFavoritesPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteGroupedByRequest = repository.getFavoritePhotos().groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            favoritePhotos.postValue(favoriteGroupedByRequest)
        }
    }

    fun deleteFromFavoritePhotos(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromFavoritePhoto(id)
            val favoriteGroupedByRequest = repository.getFavoritePhotos().groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            if (favoriteGroupedByRequest.isEmpty()) {
                noFavoritePhotos.postValue(true)
            } else {
                favoritePhotos.postValue(favoriteGroupedByRequest)
            }
        }
    }
}