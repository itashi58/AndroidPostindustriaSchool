package com.example.androidpostindustriaschool.ui.activities.favorites.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import com.example.androidpostindustriaschool.ui.activities.favorites.view.FavoritesPhotoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritesViewModel(private val repository: FavoritePhotosRepository) : ViewModel() {
    private val _favoritePhotos: MutableLiveData<ArrayList<FavoritesPhotoAdapter.RecyclerItemData>> =
        MutableLiveData()
    private val _noFavoritePhotos: MutableLiveData<Boolean> = MutableLiveData()

    val favoritePhotos: LiveData<ArrayList<FavoritesPhotoAdapter.RecyclerItemData>>
        get() = _favoritePhotos
    val noFavoritePhotos: LiveData<Boolean>
        get() = _noFavoritePhotos



    fun getFavoritesPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteGroupedByRequest = repository.getFavoritePhotos().groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            _favoritePhotos.postValue(createDataForAdapter(favoriteGroupedByRequest))
        }
    }

    fun deleteFromFavoritePhotos(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromFavoritePhoto(id)
            val favoriteGroupedByRequest = repository.getFavoritePhotos().groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            if (favoriteGroupedByRequest.isEmpty()) {
                _noFavoritePhotos.postValue(true)
            } else {
                _favoritePhotos.postValue(createDataForAdapter(favoriteGroupedByRequest))
            }
        }
    }

    private fun createDataForAdapter(favoriteGroupedByRequest: Map<String, List<FavoritePhoto>>): ArrayList<FavoritesPhotoAdapter.RecyclerItemData> {
        val adapterData = ArrayList<FavoritesPhotoAdapter.RecyclerItemData>()
        favoriteGroupedByRequest.forEach { entry ->
            adapterData.add(FavoritesPhotoAdapter.RecyclerItemData(entry.key))
            entry.value.forEach { photo ->
                adapterData.add(FavoritesPhotoAdapter.RecyclerItemData(photo))
            }
        }
        return adapterData
    }

}