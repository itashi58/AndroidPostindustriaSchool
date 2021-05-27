package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.database.daos.FavoritePhotoDao
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto


class FavoritePhotosRepository(private val favoritePhotoDao: FavoritePhotoDao) {
    suspend fun insertInFavoritePhotos(favoritePhoto: FavoritePhoto) {
        favoritePhotoDao.insert(favoritePhoto)
    }

    suspend fun deleteFromFavoritePhotos(id: String) {
        favoritePhotoDao.delete(id)
    }

    suspend fun isInFavoritePhotos(id: String): Int {
        return favoritePhotoDao.checkForExistence(id)
    }

    suspend fun deleteFromFavoritePhoto(id: String) {
        favoritePhotoDao.delete(id)
    }

    suspend fun getFavoritePhotos(): List<FavoritePhoto> {
        return favoritePhotoDao.getAllChosenPhotos()
    }
}