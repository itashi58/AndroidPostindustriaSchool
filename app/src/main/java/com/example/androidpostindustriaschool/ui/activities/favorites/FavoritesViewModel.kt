package com.example.androidpostindustriaschool.ui.activities.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritesViewModel(private val repository: FavoritePhotosRepository) : ViewModel() {
    val favoritePhotos: MutableLiveData<ArrayList<FavoritesPhotoAdapter.RecyclerItemData>> =
        MutableLiveData()
    val noFavoritePhotos: MutableLiveData<Boolean> = MutableLiveData()

    fun getFavoritesPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteGroupedByRequest = repository.getFavoritePhotos().groupBy { chosenPhoto ->
                chosenPhoto.request
            }
            favoritePhotos.postValue(createDataForAdapter(favoriteGroupedByRequest))
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
                favoritePhotos.postValue(createDataForAdapter(favoriteGroupedByRequest))
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