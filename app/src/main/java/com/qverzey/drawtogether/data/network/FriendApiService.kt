package com.qverzey.drawtogether.data.network

import com.qverzey.drawtogether.data.model.FriendInfo
import com.qverzey.drawtogether.data.model.FriendRequestInfo
import com.qverzey.drawtogether.data.model.ResponseInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendApiService {
    @GET("users/{user_id}/friends")
    suspend fun getFriends(@Path("user_id") userId: String): List<FriendInfo>

    @POST("users/{user_id}/friends/add")
    suspend fun addFriend(@Path("user_id") userId: String, @Body info: FriendRequestInfo): ResponseInfo

    @POST("users/{user_id}/friends/remove")
    suspend fun removeFriend(@Path("user_id") userId: String, @Body info: FriendRequestInfo): ResponseInfo
}