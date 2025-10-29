package com.qverzey.drawtogether.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qverzey.drawtogether.data.model.FriendInfo
import com.qverzey.drawtogether.data.model.FriendRequestInfo
import com.qverzey.drawtogether.data.model.ProfileUpdate
import com.qverzey.drawtogether.data.model.ResponseInfo
import com.qverzey.drawtogether.data.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class FriendUiState {
    object Idle : FriendUiState()
    object Loading : FriendUiState()
    data class Success(val friends: List<FriendInfo>) : FriendUiState()
    data class Message(val info: ResponseInfo) : FriendUiState()
    data class Error(val message: String) : FriendUiState()
}

class FriendViewModel(
    private val repository: FriendRepository = FriendRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<FriendUiState>(FriendUiState.Idle)
    val uiState: StateFlow<FriendUiState> = _uiState

    fun getFriends(userId: String) {
        viewModelScope.launch {
            _uiState.value = FriendUiState.Loading
            try {
                val friends = repository.getFriends(userId)
                _uiState.value = FriendUiState.Success(friends)
            } catch (e: Exception) {
                _uiState.value = FriendUiState.Error("Failed to load friends: ${e.message}")
            }
        }
    }

    fun addFriend(userId: String, info: FriendRequestInfo) {
        viewModelScope.launch {
            _uiState.value = FriendUiState.Loading
            try {
                val response = repository.addFriend(userId, info)
                _uiState.value = FriendUiState.Message(response)
            } catch (e: Exception) {
                _uiState.value = FriendUiState.Error("Failed to add friend: ${e.message}")
            }
        }
    }

    fun removeFriend(userId: String, info: FriendRequestInfo) {
        viewModelScope.launch {
            _uiState.value = FriendUiState.Loading
            try {
                val response = repository.removeFriend(userId, info)
                _uiState.value = FriendUiState.Message(response)
            } catch (e: Exception) {
                _uiState.value = FriendUiState.Error("Failed to remove friend: ${e.message}")
            }
        }
    }
}