package com.qverzey.drawtogether.data.network

import com.qverzey.drawtogether.data.model.Post
import retrofit2.http.GET

interface ImageApiService {
    @GET("images")
    suspend fun getImages(): List<Post>
}