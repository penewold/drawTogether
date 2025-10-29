package com.qverzey.drawtogether.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qverzey.drawtogether.ui.viewModels.PostViewModel
import com.qverzey.drawtogether.ui.viewModels.SessionState
import com.qverzey.drawtogether.ui.viewModels.UserViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.qverzey.drawtogether.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    session: SessionState.LoggedIn,
    userViewModel: UserViewModel = viewModel()
) {
    val messages = listOf(
        stringResource(R.string.msg1),
        stringResource(R.string.msg2),
        stringResource(R.string.msg3),
        stringResource(R.string.msg4),
        stringResource(R.string.msg5)
    )


    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        BubblesLayer(messages)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Greeting message at the top
            Text(
                text = stringResource(R.string.hey_ready_to_create, session.userId),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            // Center-aligned button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(16.dp)
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.surprise_me),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun BubblesLayer(messages: List<String>) {
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val widthPx = with(density) { config.screenWidthDp.dp.toPx() - 200 }
    val heightPx = with(density) { config.screenHeightDp.dp.toPx() }

    var bubbles by remember {
        mutableStateOf(
            List(10) {
                BubbleData(
                    x = Random.nextFloat() * widthPx,
                    y = Random.nextFloat() * heightPx,
                    size = Random.nextInt(60, 130).dp
                )
            }
        )
    }

    var poppedMessages by remember { mutableStateOf(listOf<PoppedMessage>()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Draw bubbles
        bubbles.forEach { bubble ->
            Bubble(
                bubble = bubble,
                onClick = {
                    // Pop bubble and show message
                    val msg = messages.random()
                    poppedMessages = poppedMessages + PoppedMessage(msg, bubble.x - 20, bubble.y)
                    bubbles = bubbles - bubble
                }
            )
        }

        // Show transient popped messages
        poppedMessages.forEach { msg ->
            AnimatedMessage(msg) {
                poppedMessages = poppedMessages - msg
            }
        }
    }
}

data class BubbleData(val x: Float, val y: Float, val size: Dp)
data class PoppedMessage(val text: String, val x: Float, val y: Float)

@Composable
fun Bubble(bubble: BubbleData, onClick: () -> Unit) {

        Image(
            painterResource(R.drawable.bubble000),
            contentDescription = "bubble",
            modifier = Modifier
                .absoluteOffset(x = bubble.x.toDp(), y = bubble.y.toDp())
                .size(bubble.size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                .clickable { onClick() }
        )

}

@Composable
fun AnimatedMessage(msg: PoppedMessage, onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000)
        visible = false
        delay(400)
        onFinished()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(200)) + slideInVertically { it / 4 },
        exit = fadeOut(animationSpec = tween(400)) + slideOutVertically { -it / 2 },
        modifier = Modifier
            .absoluteOffset(x = msg.x.dp, y = msg.y.toDp())
            .wrapContentSize(Alignment.Center)
            .width(120.dp)
    ) {
        Text(
            text = msg.text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .alpha(0.9f)
        )
    }
}

@Composable
private fun Float.toDp(): Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}