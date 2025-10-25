package com.qverzey.drawtogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.qverzey.drawtogether.ui.screens.SignupScreen
import com.qverzey.drawtogether.ui.theme.DefaultTheme

class SignupActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultTheme {
                SignupScreen()
            }
        }
    }
}