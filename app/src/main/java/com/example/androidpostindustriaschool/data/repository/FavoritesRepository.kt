package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.database.daos.ChosenPhotoDao
import com.example.androidpostindustriaschool.data.database.model.ChosenPhoto


class FavoritesRepository(private val chosenPhotoDao: ChosenPhotoDao) {
    suspend fun insertInChosenPhotoDB(chosenPhoto: ChosenPhoto) {
        chosenPhotoDao.insert(chosenPhoto)
    }

    suspend fun deleteFromChosenPhotoDB(id: String) {
        chosenPhotoDao.delete(id)
    }

    suspend fun isInChosenPhotoDB(id: String): Int {
        return chosenPhotoDao.checkForExistence(id)
    }

    suspend fun getFavoritesPhoto(): List<ChosenPhoto>? {
        return chosenPhotoDao.getAllChosenPhotos()
    }
}