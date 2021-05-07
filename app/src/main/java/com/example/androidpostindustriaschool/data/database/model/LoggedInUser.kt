package com.example.androidpostindustriaschool.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class LoggedInUser(
        @PrimaryKey @ColumnInfo(name = "Login") val userId: String,
        @ColumnInfo(name = "Password") val displayName: String
)



