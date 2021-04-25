package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.dataClases.Data
import com.example.androidpostindustriaschool.data.RetrofitInstance

// TODO: 4/22/21 Better to pass `api` as parameter in repository. This way repository can be instantiated with different apis if needed
class Repository {
    // TODO: 4/22/21 You can map response in repository and return more useful model for presentation layer
    //  There is no error handling, so basically any network error will crash the app
    suspend fun getAPIService(search: String): Data {
        return RetrofitInstance.api.search(search)
    }
}