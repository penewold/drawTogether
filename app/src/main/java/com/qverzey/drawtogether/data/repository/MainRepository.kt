package com.qverzey.drawtogether.data.repository

import com.qverzey.drawtogether.data.model.ImageData
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.model.Profile
import com.qverzey.drawtogether.data.model.ProfileCreationInfo
import com.qverzey.drawtogether.data.model.ProfileUpdate
import com.qverzey.drawtogether.data.model.ResponseInfo
import com.qverzey.drawtogether.data.network.RetrofitInstance
import retrofit2.http.Body
import retrofit2.http.POST

class MainRepository {
    suspend fun getUser(userId: String): Profile {
        return RetrofitInstance.api.getUser(userId)
    }

    suspend fun getPosts(userId: String): List<Post> {
        return RetrofitInstance.api.getPosts(userId)
    }

    suspend fun getImages(): List<Post> {
        return RetrofitInstance.api.getImages()
    }

    suspend fun getUsers(): List<Profile> {
        return RetrofitInstance.api.getUsers()
    }

    suspend fun createUser(profileData: ProfileCreationInfo): ResponseInfo {
        return RetrofitInstance.api.createUser(profileData)
    }

    suspend fun updateProfile(info: ProfileUpdate): ResponseInfo {
        return RetrofitInstance.api.updateProfile(info)
    }

    suspend fun addPost(password: String, imageData: ImageData): ResponseInfo {
        return RetrofitInstance.api.addPost(password, imageData)
    }
}