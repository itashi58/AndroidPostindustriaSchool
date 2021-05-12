package com.example.androidpostindustriaschool.ui.favorites_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.data.repository.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritesViewModel(private val repository: FavoritesRepository) : ViewModel() {
    val favoritePhotos: MutableLiveData<Map<String, List<FavoritePhoto>>> = MutableLiveData()

    fun getFavoritesPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteGroupedByRequest = repository.getFavoritesPhoto().groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            favoritePhotos.postValue(favoriteGroupedByRequest)
        }
    }

    fun deleteFromChosenPhoto(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromChosenPhotoDB(id)
            // TODO: 5/12/21 below code block is the same as above
            val favoriteGroupedByRequest = repository.getFavoritesPhoto().groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            favoritePhotos.postValue(favoriteGroupedByRequest)
        }
    }
}