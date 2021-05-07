package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.database.daos.PhotoDao
import com.example.androidpostindustriaschool.data.database.daos.RequestHistoryDao
import com.example.androidpostindustriaschool.data.flickr.RetrofitInstanceFlickr
import com.example.androidpostindustriaschool.data.database.model.Photo
import java.io.IOException

class MainRepository(private val photoDao: PhotoDao, private val historyDao: RequestHistoryDao) {
    private val flickrApi = RetrofitInstanceFlickr.api

    /**
     * constructing urls from ApiResponse and unite them in one String
     * in case of no result for the search returns empty string ("")
     * in case of network errors will return null
     * both cases are handled in MainViewModel
     */
    suspend fun getFlickrAPIService(searchRequest: String): ArrayList<String>? {
        return try {
            val apiResponse = flickrApi.search(searchRequest)
            val urls = ArrayList<String>()
            apiResponse.photos.photo.forEach {
                urls.add("https://farm" + it.farm + ".staticflickr.com/" + it.server + "/" + it.id + "_" + it.secret + ".jpg" + "\n")
            }
            urls
        } catch (e: IOException) {
            null
        }
    }

    suspend fun insertDB(photos: ArrayList<String>, request: String) {
        for (i in photos.indices) {
            val photo = Photo(photos[i] + request, photos[i], request)
            photoDao.insert(photo)
        }
    }

    suspend fun deleteAllFromDB() {
        photoDao.deleteAll()
    }

    suspend fun deleteFromDB(id: String) {
        photoDao.delete(id)
    }

    suspend fun insertInHistoryDB(request: String) {
        historyDao.insert(request)
    }
}