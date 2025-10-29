package com.qverzey.drawtogether.ui.screens

import android.content.Intent
import android.se.omapi.Session
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.qverzey.drawtogether.EditProfileActivity
import com.qverzey.drawtogether.R
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.ui.viewModels.PostViewModel
import com.qverzey.drawtogether.ui.viewModels.SessionState
import com.qverzey.drawtogether.ui.viewModels.UserUiState
import com.qverzey.drawtogether.ui.viewModels.UserViewModel

@Preview(showBackground = true)
@Composable
fun ShowcaseProfileScreen(
    contentPadding: PaddingValues = PaddingValues.Zero,
    session: SessionState.LoggedIn = SessionState.LoggedIn("testing", "password"),
    viewingProfile: String = "testing"
) {
    val userViewModel: UserViewModel = viewModel()
    LaunchedEffect(Unit) {
        userViewModel.getUser(viewingProfile)
    }

    val userUiState by userViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(16.dp))

        // Profile info
        when (userUiState) {
            is UserUiState.Loading -> CircularProgressIndicator()
            is UserUiState.Success -> {
                val user = (userUiState as UserUiState.Success).user
                Log.i("profile", "$user")
                Image(
                    painter = if (user?.image != null && user.image != "") rememberAsyncImagePainter(user.image) else painterResource(
                        R.drawable.default_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = if (user?.displayName != null) user.displayName else "Not available",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            is UserUiState.Error -> Text((userUiState as UserUiState.Error).message)
            else -> {}
        }

        Text(text = "@${viewingProfile}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(8.dp))

        when (userUiState) {
            is UserUiState.Error -> Text("Error loading posts", color = MaterialTheme.colorScheme.error)
            UserUiState.Loading -> CircularProgressIndicator()
            is UserUiState.Success -> {
                val user = (userUiState as UserUiState.Success).user
                if (user != null) PostsGrid(user.posts)

            }
            else -> {}
        }
    }
}

