@file:OptIn(ExperimentalMaterial3Api::class)
package com.qverzey.drawtogether.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.qverzey.drawtogether.MainActivity
import com.qverzey.drawtogether.R
import com.qverzey.drawtogether.data.model.ProfileUpdate
import com.qverzey.drawtogether.ui.viewModels.PostViewModel
import com.qverzey.drawtogether.ui.viewModels.ProfileViewModel
import com.qverzey.drawtogether.ui.viewModels.SessionState
import com.qverzey.drawtogether.ui.viewModels.UserUiState
import com.qverzey.drawtogether.ui.viewModels.UserViewModel
import java.io.ByteArrayOutputStream
import java.util.Base64
import kotlin.math.min
import androidx.core.graphics.scale
import com.qverzey.drawtogether.EditProfileActivity


@Composable
fun EditProfileScreen(
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

    val displayNameState = rememberTextFieldState("")
    val bioState = rememberTextFieldState("")
    var visibility by remember {mutableStateOf(false)}
    var isChanged by remember { mutableStateOf(false) }
    var originalDisplayName by remember { mutableStateOf("") }
    var originalBio by remember { mutableStateOf("") }
    var originalVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(userUiState) {
        if (userUiState is UserUiState.Success) {
            val name = (userUiState as UserUiState.Success).user?.displayName ?: ""
            val bio = (userUiState as UserUiState.Success).user?.bio ?: ""
            val gottenVisibility = (userUiState as UserUiState.Success).user?.profileVisibility ?: false
            originalDisplayName = name
            originalBio = bio
            originalVisibility = gottenVisibility
            displayNameState.edit {
                replace(0, length, originalDisplayName)
            }
            bioState.edit {
                replace(0, length, originalBio)
            }
            visibility = originalVisibility
        }
    }

    LaunchedEffect(displayNameState.text, bioState.text, visibility) {
        isChanged = displayNameState.text != originalDisplayName || bioState.text != originalBio || visibility != originalVisibility
    }

    val context = LocalContext.current

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var base64Image by remember { mutableStateOf<String?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val (processedBitmap, base64) = processImageToBase64(context, it)
            bitmap = processedBitmap
            base64Image = base64
        }
    }


    var showExitDialog by remember { mutableStateOf(false) }

    val profileImage: Painter = if ((userUiState as? UserUiState.Success)?.user?.image == null) {
        painterResource(R.drawable.default_profile)
    } else {
        rememberAsyncImagePainter((userUiState as? UserUiState.Success)?.user?.image)
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
                    Text(stringResource(R.string.edit_profile))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!isChanged) {
                                val intent = Intent(context, MainActivity::class.java)
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
                        onClick = {
                            val profileUpdate = ProfileUpdate(
                                password = session.password,
                                displayName = displayNameState.text as String,
                                bio = bioState.text as String,
                                profilePicture = base64Image ?: (userUiState as UserUiState.Success).user?.image ?: "",
                                profileVisibility = visibility
                                )
                            userViewModel.updateUser(session.userId, profileUpdate)
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                                  },
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
            Box (
                modifier = Modifier
                    .clickable {
                        pickImageLauncher.launch("image/*")
                    }
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(

                        painter = profileImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

            }

            OutlinedTextField(
                state = displayNameState,
                label = { Text(stringResource(R.string.display_name)) },
            )

            OutlinedTextField(
                state = bioState,
                label = { Text(stringResource(R.string.bio)) },
                lineLimits = TextFieldLineLimits.MultiLine(5, 10),
            )

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.public_profile))
                Checkbox(
                    visibility,
                    {visibility = it}
                )
            }
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
        title = { Text(stringResource(R.string.unsaved_changes)) },
        text = { Text(stringResource(R.string.unsaved_changes_body)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.exit))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

fun processImageToBase64(context: Context, imageUri: Uri): Pair<Bitmap, String> {
    // Load bitmap
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val originalBitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    // ✅ Scale down (e.g. max 512px)
    val maxSize = 512
    val ratio = min(
        maxSize.toFloat() / originalBitmap.width,
        maxSize.toFloat() / originalBitmap.height
    )
    val width = (originalBitmap.width * ratio).toInt()
    val height = (originalBitmap.height * ratio).toInt()
    val scaledBitmap = originalBitmap.scale(width, height)

    // ✅ Crop to square (center)
    val cropSize = min(width, height)
    val xOffset = (width - cropSize) / 2
    val yOffset = (height - cropSize) / 2
    val croppedBitmap = Bitmap.createBitmap(scaledBitmap, xOffset, yOffset, cropSize, cropSize)

    // ✅ Convert to Base64
    val outputStream = ByteArrayOutputStream()
    croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    val bytes = outputStream.toByteArray()

    return croppedBitmap to ("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes))
}
