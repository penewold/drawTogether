package com.qverzey.drawtogether.data.repository

import com.qverzey.drawtogether.data.model.FriendInfo
import com.qverzey.drawtogether.data.model.FriendRequestInfo
import com.qverzey.drawtogether.data.model.ResponseInfo
import com.qverzey.drawtogether.data.network.RetrofitInstance

class FriendRepository {
    suspend fun getFriends(id: String): List<FriendInfo> =
        RetrofitInstance.friendApi.getFriends(id)

    suspend fun addFriend(id: String, friendRequestInfo: FriendRequestInfo): ResponseInfo =
        RetrofitInstance.friendApi.addFriend(id, friendRequestInfo)

    suspend fun removeFriend(id: String, friendRequestInfo: FriendRequestInfo): ResponseInfo =
        RetrofitInstance.friendApi.removeFriend(id, friendRequestInfo)
}