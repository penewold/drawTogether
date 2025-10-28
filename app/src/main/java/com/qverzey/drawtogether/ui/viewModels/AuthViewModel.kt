package com.qverzey.drawtogether.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qverzey.drawtogether.data.local.SessionManager
import com.qverzey.drawtogether.data.model.Profile
import com.qverzey.drawtogether.data.model.ProfileCreationInfo
import com.qverzey.drawtogether.data.model.ResponseInfo
import com.qverzey.drawtogether.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: Profile?) : AuthUiState()
    data class Message(val info: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    // Create account
    fun signup(displayName: String, username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val request = ProfileCreationInfo(username, displayName, password)
                val response = repository.createUser(request)

                AuthUiState.Error("????? response.message and .error was null??")
                if (response.message != null) {
                    _uiState.value = AuthUiState.Message(response.message)
                if (response.error != null)
                    _uiState.value = AuthUiState.Error(response.error)
                }


            } catch (e: HttpException) {  // <- Retrofit error with non-2xx code
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    // Try to parse it as JSON (optional)
                    JSONObject(errorBody ?: "{}").optString("error", "Unknown error")
                } catch (_: Exception) {
                    "Server returned an error"
                }
                _uiState.value = AuthUiState.Error(errorMessage)
            } catch (e: IOException) {  // <- network or timeout issue
                _uiState.value = AuthUiState.Error("Network error: check your connection")
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }


    fun login(userId: String, password: String, context: Context) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                SessionManager(context).saveSession(userId, password)
                _uiState.value = AuthUiState.Success(repository.getUser(userId))
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}