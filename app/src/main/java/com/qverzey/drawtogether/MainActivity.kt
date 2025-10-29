package com.qverzey.drawtogether

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qverzey.drawtogether.data.local.SessionManager
import com.qverzey.drawtogether.ui.screens.EditProfileScreen
import com.qverzey.drawtogether.ui.screens.GalleryScreen
import com.qverzey.drawtogether.ui.screens.MessagesScreen
import com.qverzey.drawtogether.ui.screens.ProfileScreen
import com.qverzey.drawtogether.ui.screens.SettingScreen
import com.qverzey.drawtogether.ui.theme.DefaultTheme
import com.qverzey.drawtogether.ui.viewModels.SessionState
import com.qverzey.drawtogether.ui.viewModels.SessionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)
        val factory = SessionViewModelFactory(sessionManager)
        setContent {
            DefaultTheme {
                val context = LocalContext.current
                val sessionViewModel: SessionViewModel = viewModel(factory = factory)
                val sessionState by sessionViewModel.sessionState.collectAsState()
                when (sessionState) {
                    is SessionState.Loading -> Text("Loading")
                    is SessionState.LoggedOut -> {
                        val intent = Intent(context, SignupActivity::class.java)
                        context.startActivity(intent)
                    }
                    is SessionState.LoggedIn -> {
                        val session = sessionState as SessionState.LoggedIn
                        MainScreen(session)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DefaultTheme {
        MainScreen(SessionState.LoggedIn("hello", "hello"))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(session: SessionState.LoggedIn) {
    val selectedIndex = remember { mutableIntStateOf(2) }

    Scaffold(
        bottomBar = {
            BottomNavBar(selected = selectedIndex)
        }
    ) { paddingValues ->
        // Main content
        InsideScreen(selectedIndex.intValue, contentPadding = paddingValues, session)
    }

}



@Composable
fun BottomNavBar(selected: MutableState<Int>) {

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = selected.value == 0,
            onClick = { selected.value = 0 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Photo, contentDescription = "Gallery") },
            label = { Text("Gallery") },
            selected = selected.value == 1,
            onClick = { selected.value = 1 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selected.value == 2,
            onClick = { selected.value = 2 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "Messages") },
            label = { Text("Messages") },
            selected = selected.value == 3,
            onClick = { selected.value = 3 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selected.value == 4,
            onClick = { selected.value = 4 }
        )
    }


}

@Composable
fun InsideScreen(screenIndex: Int = 2, contentPadding: PaddingValues = PaddingValues(0.dp), session: SessionState.LoggedIn) {
    when (screenIndex) {
        0 -> SettingScreen(contentPadding, session)
        1 -> GalleryScreen(contentPadding)
        2 -> PlaceholderScreen(contentPadding)
        3 -> MessagesScreen(contentPadding, session)
        4 -> ProfileScreen(contentPadding, session)
    }
}

@Composable
fun PlaceholderScreen(contentPadding: PaddingValues = PaddingValues(0.dp)) {
    Text(
        "Placeholder",
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)

    )
}