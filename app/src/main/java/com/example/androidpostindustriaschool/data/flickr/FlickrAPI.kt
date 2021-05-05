package com.example.androidpostindustriaschool.data.flickr

import com.example.androidpostindustriaschool.data.flickr.model.PhotoResponse
import retrofit2.http.*


interface FlickrAPI {

    @GET("services/rest/?method=flickr.photos.search")
    suspend fun search(@Query("text") search: String): PhotoResponse

}