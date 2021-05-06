package com.example.androidpostindustriaschool.data.database.daos

import androidx.room.*
import com.example.androidpostindustriaschool.data.database.model.ChosenPhoto
import com.example.androidpostindustriaschool.data.database.model.Photo


@Dao
interface ChosenPhotoDao {

    @Query("DELETE FROM chosen_photo_table WHERE ID = :id")
    suspend fun delete(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: ChosenPhoto)

    @Query("SELECT * FROM chosen_photo_table")
    fun getAllChosenPhotos(): List<Photo>?

    @Query("SELECT EXISTS(SELECT * FROM chosen_photo_table WHERE ID = :id)")
    suspend fun checkForExistence(id: String):Int
}