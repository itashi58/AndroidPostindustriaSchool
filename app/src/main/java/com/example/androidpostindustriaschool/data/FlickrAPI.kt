package com.example.androidpostindustriaschool.data

import com.example.androidpostindustriaschool.data.dataClases.Data
import com.example.androidpostindustriaschool.util.Constants.Companion.API_FLICKR_KEY
import retrofit2.http.*


interface FlickrAPI {

    // TODO: 4/22/21 query  params format=json&nojsoncallback=1&api_key=${API_FLICKR_KEY} can be placed in request interceptor.
    //  Read about custom interceptors and how to add query args to each request
    //  With your current approach you will have to duplicate this params over and over again.
    @GET("services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&api_key=${API_FLICKR_KEY}")
    suspend fun search(@Query("text") search: String): Data

}