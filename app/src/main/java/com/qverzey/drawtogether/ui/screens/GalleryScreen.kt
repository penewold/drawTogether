package com.qverzey.drawtogether.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.ui.viewModels.ImageUiState
import com.qverzey.drawtogether.ui.viewModels.ImageViewModel
import com.qverzey.drawtogether.ui.viewModels.PostViewModel
import com.qverzey.drawtogether.ui.viewModels.UserViewModel

@Composable
fun GalleryScreen(
    contentPadding: PaddingValues = PaddingValues.Zero,
    imageViewModel: ImageViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        imageViewModel.getImages()
    }

    val imageUiState by imageViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (imageUiState) {
            is ImageUiState.Error -> Text("Error occured: ${(imageUiState as ImageUiState.Error).message}")
            ImageUiState.Idle -> CircularProgressIndicator()
            ImageUiState.Loading -> CircularProgressIndicator()
            is ImageUiState.Success -> PostsGrid((imageUiState as ImageUiState.Success).images)
        }

    }
}

