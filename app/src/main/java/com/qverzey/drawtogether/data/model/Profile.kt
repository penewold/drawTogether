package com.qverzey.drawtogether.data.model

data class Profile(
    val displayName: String,
    val bio: String,
    val image: String,
    val posts: List<Post>,
    val profileVisibility: Boolean
)