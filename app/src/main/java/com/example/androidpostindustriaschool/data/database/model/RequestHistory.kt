package com.example.androidpostindustriaschool.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request_history_table")
data class RequestHistory(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val Id: Int,
    @ColumnInfo(name = "Request") val request: String
)
