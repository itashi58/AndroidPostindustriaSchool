package com.example.androidpostindustriaschool.ui.favorites_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.ChosenPhoto
import com.example.androidpostindustriaschool.data.repository.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritesViewModel(private val repository: FavoritesRepository) : ViewModel() {
    val favoritePhotos: MutableLiveData<Map<String, List<ChosenPhoto>>> = MutableLiveData()

    fun getFavoritesPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteGroupedByRequest = repository.getFavoritesPhoto()?.groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            favoritePhotos.postValue(favoriteGroupedByRequest)
        }
    }

    fun deleteFromChosenPhoto(id:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromChosenPhotoDB(id)
            val favoriteGroupedByRequest = repository.getFavoritesPhoto()?.groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            favoritePhotos.postValue(favoriteGroupedByRequest)
        }
    }
}