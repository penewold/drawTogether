package com.qverzey.drawtogether.data.network

import com.qverzey.drawtogether.data.model.AddPostRequest
import com.qverzey.drawtogether.data.model.ImageData
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.model.ResponseInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApiService {

    @GET("users/{user_id}/posts")
    suspend fun getUserPosts(@Path("user_id") userId: String): List<Post>

    @POST("users/{user_id}/posts/add")
    suspend fun addPost(
        @Path("user_id") userId: String,
        @Body request: AddPostRequest
    ): ResponseInfo

}
