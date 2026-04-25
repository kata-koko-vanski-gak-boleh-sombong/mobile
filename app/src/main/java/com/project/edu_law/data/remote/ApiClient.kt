package com.project.edu_law.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://10.0.175.140:3000/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .build()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}