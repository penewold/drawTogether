package com.qverzey.drawtogether.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ImageUiState {
    object Idle : ImageUiState()
    object Loading : ImageUiState()
    data class Success(val images: List<Post>) : ImageUiState()
    data class Error(val message: String) : ImageUiState()
}

class ImageViewModel(
    private val repository: ImageRepository = ImageRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ImageUiState>(ImageUiState.Idle)
    val uiState: StateFlow<ImageUiState> = _uiState

    fun getImages() {
        viewModelScope.launch {
            _uiState.value = ImageUiState.Loading
            try {
                val images = repository.getImages()
                _uiState.value = ImageUiState.Success(images)
            } catch (e: Exception) {
                _uiState.value = ImageUiState.Error("Failed to load images: ${e.message}")
            }
        }
    }
}
