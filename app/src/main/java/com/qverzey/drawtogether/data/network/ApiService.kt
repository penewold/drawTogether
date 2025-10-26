package com.qverzey.drawtogether.data.network

import com.qverzey.drawtogether.data.model.ImageData
import com.qverzey.drawtogether.data.model.ResponseInfo
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.model.Profile
import com.qverzey.drawtogether.data.model.ProfileCreationInfo
import com.qverzey.drawtogether.data.model.ProfileUpdate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("users/{user_id}")
    suspend fun getUser(@Path("user_id") userId: String): Profile

    @GET("users/{user_id}/posts")
    suspend fun getPosts(@Path("user_id") userId: String): List<Post>

    @GET("images")
    suspend fun getImages(): List<Post>

    @GET("users")
    suspend fun getUsers(): List<Profile>

    @POST("users/create")
    suspend fun createUser(@Body info: ProfileCreationInfo): ResponseInfo

    @POST("users/{user_id}/update")
    suspend fun updateProfile(@Body info: ProfileUpdate): ResponseInfo

    @POST("users/{user_id}/posts/add")
    suspend fun addPost(@Body password: String, @Body imageData: ImageData): ResponseInfo
}