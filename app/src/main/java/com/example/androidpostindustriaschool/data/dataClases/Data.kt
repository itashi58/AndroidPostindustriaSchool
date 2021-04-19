package com.example.androidpostindustriaschool.data.dataClases
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("photos")
    @Expose
    var photos: Photos,

    @SerializedName("stat")
    @Expose
    var stat: String
)