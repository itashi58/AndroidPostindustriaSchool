package com.example.androidpostindustriaschool.data.flickr.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhotoResponse(
        @SerializedName("photos")
        @Expose
        var photos: Photos,

        @SerializedName("stat")
        @Expose
        var stat: String
)