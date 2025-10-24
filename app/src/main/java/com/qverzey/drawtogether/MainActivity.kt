package com.qverzey.drawtogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qverzey.drawtogether.ui.screens.ProfileScreen
import com.qverzey.drawtogether.ui.screens.SettingScreen
import com.qverzey.drawtogether.ui.theme.DefaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultTheme {  // Use your Material 3 theme here
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(startIndex: Int = 2) {
    val selectedIndex = remember { mutableIntStateOf(startIndex) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Material 3 App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        bottomBar = {
            BottomNavBar(selected = selectedIndex)
        }
    ) { paddingValues ->
        // Main content
        InsideScreen(selectedIndex.intValue, contentPadding = paddingValues)
        /*
        Text(
            text = if (selectedIndex.intValue == 2) "Home Screen" else "Settings Screen",
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            style = MaterialTheme.typography.bodyLarge
        )*/
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DefaultTheme {
        MainScreen(0)
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
            icon = { Icon(Icons.Filled.Message, contentDescription = "Messages") },
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
fun InsideScreen(screenIndex: Int = 2, contentPadding: PaddingValues = PaddingValues(0.dp)) {


    when (screenIndex) {
        0 -> SettingScreen(contentPadding)
        1 -> PlaceholderScreen(contentPadding)
        2 -> PlaceholderScreen(contentPadding)
        3 -> PlaceholderScreen(contentPadding)
        4 -> ProfileScreen(contentPadding,"qverzey", "0001", "https://assetsio.gnwcdn.com/how-hollow-knights-community-crafted-gibberish-into-a-real-language-1567781461548.jpg?width=1200&height=1200&fit=crop&quality=100&format=png&enable=upscale&auto=webp")
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