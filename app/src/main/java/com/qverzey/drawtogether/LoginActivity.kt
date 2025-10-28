package com.qverzey.drawtogether

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.qverzey.drawtogether.ui.screens.LoginScreen
import com.qverzey.drawtogether.ui.theme.DefaultTheme

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            DefaultTheme {
                val context = LocalContext.current
                LoginScreen(onLoginSuccess = {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("destination", 2)
                    }
                    context.startActivity(intent)
                    Log.i("auth", "starting main activity")
                })
            }
        }
    }
}