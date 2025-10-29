package com.qverzey.drawtogether.data.model

data class ProfileUpdate(
    val password: String,
    val displayName: String,
    val bio: String,
    val profilePicture: String,
    val profileVisibility: Boolean
)