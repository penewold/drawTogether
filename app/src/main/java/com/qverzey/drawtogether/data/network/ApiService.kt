package com.qverzey.drawtogether.data.network

import com.qverzey.drawtogether.data.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET()
}