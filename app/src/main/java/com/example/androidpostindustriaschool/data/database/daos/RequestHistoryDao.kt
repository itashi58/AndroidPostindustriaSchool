package com.example.androidpostindustriaschool.data.database.daos

import androidx.room.*
import com.example.androidpostindustriaschool.data.database.model.RequestHistory

@Dao
interface RequestHistoryDao {

    @Query("DELETE FROM request_history_table")
    suspend fun deleteAll()

    @Query("INSERT INTO request_history_table (Request) VALUES(:request)")
    suspend fun insert(request: String)

    @Query("SELECT * FROM request_history_table ORDER BY id DESC")
    suspend fun getAllRequests(): Array<RequestHistory>
}