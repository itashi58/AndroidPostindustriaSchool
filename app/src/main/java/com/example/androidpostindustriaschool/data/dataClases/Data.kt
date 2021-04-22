package com.example.androidpostindustriaschool.data.dataClases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// TODO: 4/22/21 This class name doesn't represent what kind of "data" it stores. Rename it to something more appropriate (e.g. PhotoResponse, etc.)
data class Data(
    @SerializedName("photos")
    @Expose
    var photos: Photos,

    @SerializedName("stat")
    @Expose
    var stat: String
)