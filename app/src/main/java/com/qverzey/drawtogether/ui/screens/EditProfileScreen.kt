@file:OptIn(ExperimentalMaterial3Api::class)
package com.qverzey.drawtogether.ui.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.qverzey.drawtogether.MainActivity
import com.qverzey.drawtogether.R
import com.qverzey.drawtogether.data.model.Post
import com.qverzey.drawtogether.ui.viewModels.ProfileViewModel.ProfileUiState
import com.qverzey.drawtogether.ui.viewModels.ProfileViewModel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    userName: String,
    bio: String,
    profileImageUrl: String?,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    var usernameText by remember {mutableStateOf(userName)}
    var bioText by remember {mutableStateOf(bio)}

    val isChanged = usernameText != userName || bioText != bio

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    val profileImage: Painter = if (profileImageUrl == null) {
        painterResource(R.drawable.default_profile)
    } else {
        rememberAsyncImagePainter(profileImageUrl)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Edit profile")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!isChanged) {
                                val intent = Intent(context, MainActivity::class.java).apply {
                                    putExtra("destination", 4)
                                }
                                context.startActivity(intent)
                            } else {
                                showExitDialog = true
                            }

                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    FilledIconButton(
                        onClick = { TODO("Save thing") },
                        enabled = isChanged,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save",
                            )
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            // Profile info
            Image(
                painter = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            OutlinedTextField(
                value = usernameText,
                onValueChange = { newValue ->
                    usernameText = newValue
                },
                label = { Text("Display name") },
            )

            OutlinedTextField(
                state = rememberTextFieldState(initialText = bio),
                label = { Text("Bio") },
                lineLimits = TextFieldLineLimits.MultiLine(5, 10),
            )
            Spacer(Modifier.height(8.dp))

        }
    }
    if (showExitDialog) {
        ExitConfirmationDialog(
            onConfirm = {
                showExitDialog = false
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("destination", 4)
                }
                context.startActivity(intent)
            },
            onDismiss = { showExitDialog = false }
        )
    }
}

@Composable
fun ExitConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Unsaved Changes") },
        text = { Text("You have unsaved changes. Are you sure you want to exit?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Exit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(
        "Qverzey",
        "Hello world! This is my bio😍😍🔥🥀😭",
        null
    )
}
