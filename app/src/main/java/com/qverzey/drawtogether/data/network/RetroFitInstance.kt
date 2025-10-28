package com.qverzey.drawtogether.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.0.5:53782/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApiService by lazy { retrofit.create(UserApiService::class.java) }
    val postApi: PostApiService by lazy { retrofit.create(PostApiService::class.java) }
    val imageApi: ImageApiService by lazy { retrofit.create(ImageApiService::class.java) }
}
