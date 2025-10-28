@file:OptIn(ExperimentalMaterial3Api::class)
package com.qverzey.drawtogether.ui.screens

import android.content.Intent
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.qverzey.drawtogether.EditProfileActivity
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.ui.viewModels.PostUiState
import com.qverzey.drawtogether.ui.viewModels.PostViewModel
import com.qverzey.drawtogether.ui.viewModels.SessionState
import com.qverzey.drawtogether.ui.viewModels.UserUiState
import com.qverzey.drawtogether.ui.viewModels.UserViewModel


@Composable
fun ProfileScreen(
    contentPadding: PaddingValues,
    session: SessionState.LoggedIn,
    userViewModel: UserViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel()
) {
    LaunchedEffect(Unit) {

        userViewModel.getUser(session.userId)
        postViewModel.getUserPosts(session.userId)

    }

    val userUiState by userViewModel.uiState.collectAsState()
    val postUiState by postViewModel.uiState.collectAsState()
    val context = LocalContext.current

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
                Image(
                    painter = rememberAsyncImagePainter(user?.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            is UserUiState.Error -> Text((userUiState as UserUiState.Error).message)
            else -> {}
        }
        Image(
            painter = rememberAsyncImagePainter((userUiState as UserUiState.Success).user?.image),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = (userUiState as UserUiState.Success).user?.displayName ?: "Not available",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = "@$session.userId", style = MaterialTheme.typography.bodyMedium)
        Button(
            onClick = {
                val intent = Intent(context, EditProfileActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Edit Profile")
        }

        Spacer(Modifier.height(8.dp))

        when {
            postUiState is PostUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            userUiState is UserUiState.Error -> {
                Text(
                    text = (userUiState as UserUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            postUiState is PostUiState.Error -> {
                Text(
                    text = (postUiState as PostUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                val user = (userUiState as? UserUiState.Success)?.user

                // Only show content if session exists and user data is loaded
                if (session is SessionState.LoggedIn && user != null) {
                    PostsGrid(user.posts)

                }
            }
        }
    }
}


@Composable
fun PostsGrid(posts: List<Post>) {
    Log.i("network", "postsgrid called")

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(posts) { post ->
            Log.i("network", "post: ${post.id}, ${post.imageUrl}")
            Image(
                painter = rememberAsyncImagePainter(post.imageUrl),
                contentDescription = "Post",
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(2.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}