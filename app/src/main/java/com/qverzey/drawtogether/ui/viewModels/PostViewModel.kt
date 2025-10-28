package com.qverzey.drawtogether.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.model.ImageData
import com.qverzey.drawtogether.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PostUiState {
    object Idle : PostUiState()
    object Loading : PostUiState()
    data class Success(val posts: List<Post>) : PostUiState()
    data class Message(val info: String) : PostUiState()
    data class Error(val message: String) : PostUiState()
}

class PostViewModel(
    private val repository: PostRepository = PostRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Idle)
    val uiState: StateFlow<PostUiState> = _uiState

    fun getUserPosts(userId: String) {
        viewModelScope.launch {
            _uiState.value = PostUiState.Loading
            try {
                val posts = repository.getUserPosts(userId)
                _uiState.value = PostUiState.Success(posts)
            } catch (e: Exception) {
                _uiState.value = PostUiState.Error("Failed to load posts: ${e.message}")
            }
        }
    }

    fun addPost(userId: String, password: String, imageData: ImageData) {
        viewModelScope.launch {
            _uiState.value = PostUiState.Loading
            try {
                val response = repository.addPost(userId, password, imageData)
                _uiState.value = PostUiState.Message(response.message ?: response.error ?: "Unknown response")
            } catch (e: Exception) {
                _uiState.value = PostUiState.Error("Failed to add post: ${e.message}")
            }
        }
    }
}
