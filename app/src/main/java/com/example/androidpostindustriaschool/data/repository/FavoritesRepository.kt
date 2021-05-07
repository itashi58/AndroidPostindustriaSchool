package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.database.daos.FavoritePhotoDao
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto


class FavoritesRepository(private val favoritePhotoDao: FavoritePhotoDao) {

    suspend fun deleteFromChosenPhotoDB(id: String) {
        favoritePhotoDao.delete(id)
    }

    suspend fun getFavoritesPhoto(): List<FavoritePhoto> {
        return favoritePhotoDao.getAllChosenPhotos()
    }
}