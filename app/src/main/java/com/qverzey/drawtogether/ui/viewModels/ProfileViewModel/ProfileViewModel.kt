package com.qverzey.drawtogether.ui.viewModels.ProfileViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val posts: List<Post>) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init { loadPosts() }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val posts = repository.getPosts()
                _uiState.value = ProfileUiState.Success(posts)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Failed to load posts: ${e.message}")
            }
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun refreshPosts() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val posts = repository.getPosts()
                _uiState.value = ProfileUiState.Success(posts)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Failed to refresh: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
