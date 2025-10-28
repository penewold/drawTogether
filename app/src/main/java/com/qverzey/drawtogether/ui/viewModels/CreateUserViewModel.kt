package com.qverzey.drawtogether.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qverzey.drawtogether.data.model.ProfileCreationInfo
import com.qverzey.drawtogether.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CreateUserUiState {
    object Idle : CreateUserUiState()
    object Loading : CreateUserUiState()
    object Success : CreateUserUiState()
    data class Error(val message: String?) : CreateUserUiState()
}

class CreateUserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateUserUiState>(CreateUserUiState.Idle)
    val uiState: StateFlow<CreateUserUiState> = _uiState

    fun createUser(info: ProfileCreationInfo) {
        viewModelScope.launch {
            _uiState.value = CreateUserUiState.Loading

            try {
                val responseInfo = repository.createUser(info)

                if (responseInfo.isSuccess) {
                    _uiState.value = CreateUserUiState.Success
                } else {
                    _uiState.value = CreateUserUiState.Error(responseInfo.error ?: "Unknown error")
                }

            } catch (e: Exception) {
                _uiState.value = CreateUserUiState.Error(
                    message = "Failed to create user: ${e.message}"
                )
            }
        }
    }
}
