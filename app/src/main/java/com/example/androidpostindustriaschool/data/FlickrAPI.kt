package com.example.androidpostindustriaschool.data

import com.example.androidpostindustriaschool.data.dataClases.Data
import com.example.androidpostindustriaschool.util.Constants.Companion.API_FLICKR_KEY
import retrofit2.http.*


interface FlickrAPI {

    @GET("services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=${API_FLICKR_KEY}")
    suspend fun search(@Query("text") search: String): Data

}