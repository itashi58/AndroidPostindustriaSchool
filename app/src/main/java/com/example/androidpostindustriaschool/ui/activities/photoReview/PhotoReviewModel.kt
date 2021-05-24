package com.example.androidpostindustriaschool.ui.activities.photoReview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: 24.05.2021 `ViewModel`, not just `Model`
class PhotoReviewModel(private val repository: FavoritePhotosRepository) : ViewModel() {

    val inFavorites: MutableLiveData<Boolean> = MutableLiveData()

    fun insertInChosenPhoto(url: String, request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertInChosenPhotoDB(FavoritePhoto(url + request, url, request))
        }
    }

    fun deleteFromChosenPhoto(url: String, request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromChosenPhotoDB(url + request)
        }
    }

    fun idInChosenPhoto(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.isInChosenPhotoDB(id)) {
                // TODO: 24.05.2021 avoid hardcode
                //  can be replaced with single line inFavorites.postValue(id == 1)
                1 -> inFavorites.postValue(true)
                0 -> inFavorites.postValue(false)
                else -> {
                    Log.d("DB mistake", "incorect respone")
                    inFavorites.postValue(false)
                }
            }
        }
    }
}