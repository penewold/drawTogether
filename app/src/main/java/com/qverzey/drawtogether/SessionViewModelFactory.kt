package com.qverzey.drawtogether

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qverzey.drawtogether.data.local.SessionManager
import com.qverzey.drawtogether.ui.viewModels.SessionViewModel

class SessionViewModelFactory(
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SessionViewModel(sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
