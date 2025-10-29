package com.qverzey.drawtogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qverzey.drawtogether.data.local.SessionManager
import com.qverzey.drawtogether.ui.screens.EditProfileScreen
import com.qverzey.drawtogether.ui.theme.DefaultTheme
import com.qverzey.drawtogether.ui.viewModels.SessionState
import com.qverzey.drawtogether.ui.viewModels.SessionViewModel

class EditProfileActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val sessionManager = SessionManager(this)
            val factory = SessionViewModelFactory(sessionManager)
            val sessionViewModel: SessionViewModel = viewModel(factory = factory)
            val sessionState by sessionViewModel.sessionState.collectAsState()

            DefaultTheme {
                when (sessionState) {
                    is SessionState.Loading -> CircularProgressIndicator()
                    is SessionState.LoggedIn -> {
                        EditProfileScreen(sessionState as SessionState.LoggedIn)
                    }
                    is SessionState.LoggedOut -> {
                        Text("Log in to change your profile")
                    }
                }

            }
        }
    }
}