package com.example.androidpostindustriaschool.ui.activities.photo_review.view_model


import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import com.example.androidpostindustriaschool.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class PhotoReviewViewModel(private val repository: FavoritePhotosRepository) : ViewModel() {

    private val _photoLoaded: MutableLiveData<Boolean> = MutableLiveData()
    val photoLoaded: LiveData<Boolean>
        get() = _photoLoaded

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

    // saving image, in case such image already exists, delete it and save new one
    fun saveImage(url: String, photoDir: URI, image: Bitmap) {
        val urlWithoutSlash = url.replace('/', '|') //because '/' is counted as new directory
        val photosDir = File(photoDir)
        photosDir.mkdirs()
        val imageFile = File(photosDir, urlWithoutSlash)
        if (imageFile.exists()) imageFile.delete()
        imageFile.createNewFile()
        try {
            val out = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, Constants.SAVED_PHOTOS_QUALITY, out)
            out.flush()
            out.close()
            _photoLoaded.postValue(true)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            _photoLoaded.postValue(false)
        }
    }
}