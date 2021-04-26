package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.RetrofitInstanceFlickr
import java.io.IOException

class Repository {
    private val flickrApi = RetrofitInstanceFlickr.api

    /**
     * constructing urls from ApiResponse and unite them in one String
     * in case of no result for the search returns empty string ("")
     * in case of network errors will return null
     * both cases are handled in MainViewModel
     */
    suspend fun getFlickrAPIService(searchRequest: String): String? {
        return try {
            val apiResponse = flickrApi.search(searchRequest)
            var answerString = ""
            apiResponse.photos.photo.forEach {
                answerString += "https://farm" + it.farm + ".staticflickr.com/" + it.server + "/" + it.id + "_" + it.secret + ".jpg" + "\n"
            }
            answerString
        } catch (e: IOException) {
            null
        }
    }
}