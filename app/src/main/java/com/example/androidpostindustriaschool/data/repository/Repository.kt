package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.PhotoDao
import com.example.androidpostindustriaschool.data.RetrofitInstanceFlickr
import com.example.androidpostindustriaschool.data.modeldb.Photo
import java.io.IOException

class Repository(private val photoDao: PhotoDao) {
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
            val photo = Photo(i, photos[i], request)
            photoDao.insert(photo)
        }
    }

    suspend fun deleteAllFromDB() {
        photoDao.deleteAll()
    }

    suspend fun deleteFromDB(id: Int) {
        photoDao.delete(id)
    }
}