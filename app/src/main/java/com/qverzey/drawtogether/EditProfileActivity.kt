package com.qverzey.drawtogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.qverzey.drawtogether.ui.screens.EditProfileScreen
import com.qverzey.drawtogether.ui.theme.DefaultTheme

class EditProfileActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultTheme {
                EditProfileScreen("qverzey", "bio", null)
            }
        }
    }
}