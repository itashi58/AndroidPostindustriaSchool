package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.database.daos.FavoritePhotoDao
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto


class FavoritesRepository(private val favoritePhotoDao: FavoritePhotoDao) {

    // TODO: 5/12/21 it is not common to use uppercase for acronyms (DB)
    //  Also you can omit specifying that the photo is deleted from db, because in future you might want to extend this method to remove from local & remote storage.
    //  e.g. deleteFavouritePhoto(id)
    suspend fun deleteFromChosenPhotoDB(id: String) {
        favoritePhotoDao.delete(id)
    }

    suspend fun getFavoritesPhoto(): List<FavoritePhoto> {
        return favoritePhotoDao.getAllChosenPhotos()
    }
}