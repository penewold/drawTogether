package com.qverzey.drawtogether.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qverzey.drawtogether.data.model.Profile
import com.qverzey.drawtogether.data.model.ProfileCreationInfo
import com.qverzey.drawtogether.data.model.ProfileUpdate
import com.qverzey.drawtogether.data.model.ResponseInfo
import com.qverzey.drawtogether.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserUiState {
    object Idle : UserUiState()
    object Loading : UserUiState()
    data class Success(val user: Profile?) : UserUiState()
    data class Message(val info: ResponseInfo) : UserUiState()  // for create/update responses
    data class Error(val message: String) : UserUiState()
}

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val uiState: StateFlow<UserUiState> = _uiState

    fun getUser(userId: String) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            try {
                val user = repository.getUser(userId)
                _uiState.value = UserUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error("Failed to load user: ${e.message}")
            }
        }
    }

    fun createUser(info: ProfileCreationInfo) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            try {
                val response = repository.createUser(info)
                _uiState.value = UserUiState.Message(response)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error("Failed to create user: ${e.message}")
            }
        }
    }

    fun updateUser(userId: String, info: ProfileUpdate) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            try {
                val response = repository.updateUser(userId, info)
                _uiState.value = UserUiState.Message(response)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error("Failed to update profile: ${e.message}")
            }
        }
    }
}