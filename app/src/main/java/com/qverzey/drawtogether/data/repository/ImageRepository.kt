package com.qverzey.drawtogether.data.repository

import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.network.RetrofitInstance

class ImageRepository {
    suspend fun getImages(): List<Post> =
        RetrofitInstance.imageApi.getImages()
}
