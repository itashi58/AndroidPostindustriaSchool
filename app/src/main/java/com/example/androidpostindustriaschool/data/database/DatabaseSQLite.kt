package com.example.androidpostindustriaschool.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidpostindustriaschool.data.database.daos.FavoritePhotoDao
import com.example.androidpostindustriaschool.data.database.daos.LoginDao
import com.example.androidpostindustriaschool.data.database.model.Photo
import com.example.androidpostindustriaschool.data.database.daos.PhotoDao
import com.example.androidpostindustriaschool.data.database.daos.RequestHistoryDao
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.data.database.model.LoggedInUser
import com.example.androidpostindustriaschool.data.database.model.RequestHistory


@Database(
    entities = [Photo::class, LoggedInUser::class, FavoritePhoto::class, RequestHistory::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseSQLite : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun loginDao(): LoginDao
    abstract fun chosenPhotoDao(): FavoritePhotoDao
    abstract fun requestHistoryDao(): RequestHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseSQLite? = null

        fun getDatabase(context: Context): DatabaseSQLite {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseSQLite::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
