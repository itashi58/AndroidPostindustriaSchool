package com.example.androidpostindustriaschool.data.database.daos

import androidx.room.*
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto


@Dao
interface FavoritePhotoDao {

    @Query("DELETE FROM favorite_photo_table WHERE ID = :id")
    suspend fun delete(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: FavoritePhoto)

    @Query("SELECT * FROM favorite_photo_table")
    suspend fun getAllChosenPhotos(): List<FavoritePhoto>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_photo_table WHERE ID = :id)")
    suspend fun checkForExistence(id: String): Int
}