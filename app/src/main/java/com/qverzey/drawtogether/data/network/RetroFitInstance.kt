package com.qverzey.drawtogether.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // ports to be used: 53782, 53783
    private const val BASE_URL = "http://10.0.0.5:53782"  // Replace with your real API base URL

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}