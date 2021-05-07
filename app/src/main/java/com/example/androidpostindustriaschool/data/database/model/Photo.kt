package com.example.androidpostindustriaschool.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
data class Photo(
        //id is a url+request
        @PrimaryKey @ColumnInfo(name = "ID") val id: String,
        @ColumnInfo(name = "URL") val urls: String,
        @ColumnInfo(name = "REQUEST") val request: String
)