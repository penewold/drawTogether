package com.qverzey.drawtogether.data.repository

import com.qverzey.drawtogether.data.model.ImageData
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.model.Profile
import com.qverzey.drawtogether.data.model.ProfileCreationInfo
import com.qverzey.drawtogether.data.model.ProfileUpdate
import com.qverzey.drawtogether.data.model.ResponseInfo
import com.qverzey.drawtogether.data.network.RetrofitInstance

class UserRepository {
    suspend fun getUser(id: String): Profile =
        RetrofitInstance.userApi.getUser(id)

    suspend fun createUser(info: ProfileCreationInfo): ResponseInfo =
        RetrofitInstance.userApi.createUser(info)

    suspend fun updateUser(id: String, info: ProfileUpdate): ResponseInfo =
        RetrofitInstance.userApi.updateProfile(id, info)
}
