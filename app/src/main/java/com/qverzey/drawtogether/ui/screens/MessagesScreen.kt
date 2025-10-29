@file:OptIn(ExperimentalMaterial3Api::class)
package com.qverzey.drawtogether.ui.screens

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qverzey.drawtogether.data.model.FriendInfo
import com.qverzey.drawtogether.data.model.FriendRequestInfo
import com.qverzey.drawtogether.ui.viewModels.FriendUiState
import com.qverzey.drawtogether.ui.viewModels.FriendViewModel
import com.qverzey.drawtogether.ui.viewModels.ImageViewModel
import com.qverzey.drawtogether.ui.viewModels.SessionState
import com.qverzey.drawtogether.ui.viewModels.SessionViewModel
import com.qverzey.drawtogether.ui.viewModels.UserUiState

@Composable
fun MessagesScreen(
    contentPadding: PaddingValues = PaddingValues.Zero,
    session: SessionState.LoggedIn,
    friendViewModel: FriendViewModel = viewModel(),
    friendUiState: FriendUiState = friendViewModel.uiState.collectAsState().value
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewingProfile = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        friendViewModel.getFriends(session.userId)
    }

    Scaffold(
        topBar = {
            if (viewingProfile.value != null) {
                CenterAlignedTopAppBar({Text("@${viewingProfile.value}")},
                    navigationIcon = {
                        IconButton(
                            onClick = { viewingProfile.value = null }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) { innerPadding ->
        if (viewingProfile.value == null) {
            FriendScreenContent(
                userId = session.userId,
                password = session.password,
                friendUiState = friendUiState,
                friendViewModel = friendViewModel,
                snackbarHostState = snackbarHostState,
                modifier = Modifier.padding(innerPadding),
                onClickFriend = {viewfriend -> viewingProfile.value = viewfriend; Log.i("friend", "hello world")}
            )

        } else {
            ShowcaseProfileScreen(innerPadding, session, viewingProfile.value!!)
        }

    }
}

@Composable
private fun FriendScreenContent(
    userId: String,
    password: String,
    friendUiState: FriendUiState,
    friendViewModel: FriendViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onClickFriend: (String) -> Unit
) {
    var friendIdInput by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Friends",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- Add Friend Section ---
        OutlinedTextField(
            value = friendIdInput,
            onValueChange = { friendIdInput = it },
            label = { Text("Friend ID") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (friendIdInput.text.isNotBlank()) {
                    friendViewModel.addFriend(
                        userId,
                        FriendRequestInfo(
                            friendId = friendIdInput.text,
                            password = password
                        )
                    )
                    friendIdInput = TextFieldValue("")
                }
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text("Add Friend")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (friendUiState) {
            is FriendUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            is FriendUiState.Error -> {
                Text(
                    text = friendUiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is FriendUiState.Message -> {
                LaunchedEffect(friendUiState) {
                    snackbarHostState.showSnackbar(friendUiState.info.message ?: "")
                    // Refresh friend list if needed
                    friendViewModel.getFriends(userId)
                }
            }

            is FriendUiState.Success -> {
                FriendList(
                    friends = friendUiState.friends,
                    onRemoveFriend = { friend ->
                        friendViewModel.removeFriend(
                            userId,
                            FriendRequestInfo(
                                friendId = friend,
                                password = password
                            )
                        )
                    },
                    onClickFriend = onClickFriend
                )
            }

            FriendUiState.Idle -> {
                Button(
                    onClick = { friendViewModel.getFriends(userId) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Load Friends")
                }
            }
        }
    }
}

@Composable
fun FriendList(
    friends: List<FriendInfo>,
    onRemoveFriend: (String) -> Unit,
    onClickFriend: (String) -> Unit
) {
    if (friends.isEmpty()) {
        Text("You have no friends yet 😢", style = MaterialTheme.typography.bodyLarge)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(friends) { friend ->
                FriendCard(friend, onRemoveFriend, onClickFriend = onClickFriend)
            }
        }
    }
}

@Composable
fun FriendCard(
    friend: FriendInfo,
    onRemoveFriend: (String) -> Unit,
    onClickFriend: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(true, onClick = {
                onClickFriend(friend.id)
                Log.i("friend", friend.id)
            }),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = friend.id,
                style = MaterialTheme.typography.bodyLarge
            )
            if (!friend.confirmed) Text("Waiting...")
            TextButton(onClick = { onRemoveFriend(friend.id) }) {
                Text("Remove", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}