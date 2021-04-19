package com.example.androidpostindustriaschool.data.dataClases

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Photos (
    @SerializedName("page")
    @Expose
    var page: Int,

    @SerializedName("pages")
    @Expose
    var pages: String,

    @SerializedName("perpage")
    @Expose
    var perpage: Int,

    @SerializedName("total")
    @Expose
    var total: String,

    @SerializedName("photo")
    @Expose
    var photo: List<Photo>
)