package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.dataClases.Data
import com.example.androidpostindustriaschool.data.RetrofitInstance
import retrofit2.Call

class Repository {
    suspend fun getAPIService(search:String): Data {
        return RetrofitInstance.api.search(search)
    }
}