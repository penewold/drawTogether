package com.qverzey.drawtogether.data.repository

import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.network.RetrofitInstance

class ProfileRepository {
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}