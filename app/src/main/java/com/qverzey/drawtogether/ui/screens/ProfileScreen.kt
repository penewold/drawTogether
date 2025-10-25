@file:OptIn(ExperimentalMaterial3Api::class)
package com.qverzey.drawtogether.ui.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.qverzey.drawtogether.ui.viewModels.ProfileViewModel.ProfileUiState
import com.qverzey.drawtogether.ui.viewModels.ProfileViewModel.ProfileViewModel
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    contentPadding: PaddingValues,
    userName: String,
    userId: String,
    profileImageUrl: String,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    Column(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(16.dp))

        // Profile info
        Image(
            painter = rememberAsyncImagePainter(profileImageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = userName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = "@$userId", style = MaterialTheme.typography.bodyMedium)
        Button(
            onClick = {
                val intent = Intent(context, EditProfileActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Edit Profile")
        }

        Spacer(Modifier.height(8.dp))

        when (uiState) {
            is ProfileUiState.Loading -> {
                CircularProgressIndicator()
            }

            is ProfileUiState.Error -> {
                Text(
                    text = (uiState as ProfileUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is ProfileUiState.Success -> {
                val posts = (uiState as ProfileUiState.Success).posts

                PostsGrid(posts)
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