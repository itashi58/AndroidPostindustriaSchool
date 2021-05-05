package com.example.androidpostindustriaschool.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.androidpostindustriaschool.data.database.model.LoggedInUser

import kotlinx.coroutines.flow.Flow

@Dao
interface LoginDao {

    @Query("DELETE FROM users_table WHERE Login = :login AND Password = :password")
    suspend fun delete(login: String, password: String)

    @Query("REPLACE INTO users_table (Login, Password) VALUES(:login, :password)")
    suspend fun insert(login: String, password: String)

    @Query("SELECT * FROM users_table")
    fun getAllUsers(): Flow<List<LoggedInUser>?>

    @Query("DELETE FROM users_table")
    suspend fun deleteAll()
}