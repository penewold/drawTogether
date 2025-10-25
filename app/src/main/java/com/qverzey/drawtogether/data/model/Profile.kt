package com.qverzey.drawtogether.data.model

data class Profile(
    val userId: String,
    val displayName: String,
    val profilePicture: String,
    val posts: List<Post>
)

