@file:OptIn(ExperimentalMaterial3Api::class)
package com.qverzey.drawtogether.ui.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qverzey.drawtogether.SignupActivity
import com.qverzey.drawtogether.data.model.Profile
import com.qverzey.drawtogether.ui.viewModels.AuthUiState
import com.qverzey.drawtogether.ui.viewModels.AuthViewModel

@Composable
@Preview(showBackground = true)
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: (Profile) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val usernameTextState = rememberTextFieldState("")
    val passwordTextState = rememberTextFieldState("")

    Scaffold (

    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Login", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.padding(16.dp))
            OutlinedTextField(
                state = usernameTextState,
                label = {Text("user name")},
                modifier = Modifier.padding(4.dp)
            )
            OutlinedSecureTextField(
                state = passwordTextState,
                label = {Text("password")},
                modifier = Modifier.padding(4.dp),
            )
            Spacer(Modifier.padding(16.dp))

            Button(
                onClick = {
                    viewModel.login(
                        userId = usernameTextState.text as String,
                        password = passwordTextState.text as String,
                        context = context
                    )
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                enabled = uiState !is AuthUiState.Loading
            ) {
                Text("Log in")
            }
            when (uiState) {
                is AuthUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is AuthUiState.Error -> {
                    val msg = (uiState as AuthUiState.Error).message
                    Text(text = msg, color = MaterialTheme.colorScheme.error)
                }

                is AuthUiState.Success -> {
                    val user = (uiState as AuthUiState.Success).user
                    if (user != null) {
                        onLoginSuccess(user)
                    } else {
                        // Optional: handle edge case gracefully
                        Log.e("LoginScreen", "User is null after login")
                    }
                    viewModel.resetState()
                }

                else -> {}
            }

            ElevatedButton(
                onClick = {
                    val intent = Intent(context, SignupActivity::class.java)
                    context.startActivity(intent)
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text("Sign up instead")
            }

        }

    }
}