package com.example.androidpostindustriaschool.data.flickr

import com.example.androidpostindustriaschool.util.Constants.Companion.API_FLICKR_KEY
import com.example.androidpostindustriaschool.util.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstanceFlickr {
    private val retrofit by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val originalHttpUrl = originalRequest.url

                    val url = originalHttpUrl.newBuilder()
                            .addQueryParameter("api_key", API_FLICKR_KEY)
                            .addQueryParameter("format", "json")
                            .addQueryParameter("nojsoncallback", "1")
                            .build()

                    val request: Request = originalRequest.newBuilder()
                            .url(url)
                            .build()
                    return@addInterceptor chain.proceed(request)
                }
                .addInterceptor(httpLoggingInterceptor)
                .build()

        val gson = GsonBuilder()
                .setLenient()
                .create()

        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    val api: FlickrAPI by lazy {
        retrofit.create(FlickrAPI::class.java)
    }
}