package com.qverzey.drawtogether.data.repository

import com.qverzey.drawtogether.data.model.AddPostRequest
import com.qverzey.drawtogether.data.model.ImageData
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.model.ResponseInfo
import com.qverzey.drawtogether.data.network.RetrofitInstance

class PostRepository {
    suspend fun getUserPosts(userId: String): List<Post> =
        RetrofitInstance.postApi.getUserPosts(userId)

    suspend fun addPost(userId: String, password: String, imageData: ImageData): ResponseInfo {
        val request = AddPostRequest(password, imageData)
        return RetrofitInstance.postApi.addPost(userId, request)
    }
}
