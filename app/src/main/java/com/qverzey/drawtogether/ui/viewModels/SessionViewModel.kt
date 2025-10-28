package com.qverzey.drawtogether.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qverzey.drawtogether.data.local.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class SessionState {
    object Loading : SessionState()
    object LoggedOut : SessionState()
    data class LoggedIn(
        val userId: String,
        val password: String
    ) : SessionState()
}

class SessionViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { loggedIn ->
                if (loggedIn) {
                    val id = sessionManager.userId.first() ?: ""
                    val password = sessionManager.password.first() ?: ""
                    _sessionState.value = SessionState.LoggedIn(id, password)
                } else {
                    _sessionState.value = SessionState.LoggedOut
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _sessionState.value = SessionState.LoggedOut
        }
    }
}