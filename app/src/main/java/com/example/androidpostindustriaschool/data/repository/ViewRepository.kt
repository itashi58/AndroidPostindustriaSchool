package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.database.daos.FavoritePhotoDao
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto

class ViewRepository(private val favoritePhotoDao: FavoritePhotoDao) {
    suspend fun insertInChosenPhotoDB(favoritePhoto: FavoritePhoto) {
        favoritePhotoDao.insert(favoritePhoto)
    }

    suspend fun deleteFromChosenPhotoDB(id: String) {
        favoritePhotoDao.delete(id)
    }

    suspend fun isInChosenPhotoDB(id: String): Int {
        return favoritePhotoDao.checkForExistence(id)
    }
}