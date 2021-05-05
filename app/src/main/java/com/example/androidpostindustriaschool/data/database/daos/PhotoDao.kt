package com.example.androidpostindustriaschool.data.database.daos

import androidx.room.*
import com.example.androidpostindustriaschool.data.database.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("DELETE FROM photo_table WHERE ID = :id")
     suspend fun delete(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insert(photo: Photo)

    @Query("SELECT * FROM photo_table")
      fun getAllPhotos(): Flow<List<Photo>?>

    @Query("DELETE FROM photo_table")
     suspend fun deleteAll()

}