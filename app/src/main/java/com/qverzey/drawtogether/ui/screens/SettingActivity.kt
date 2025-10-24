@file:OptIn(ExperimentalMaterial3Api::class)
package com.qverzey.drawtogether.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingScreen(pad: PaddingValues) {
    var hasChanges by remember { mutableStateOf(false) }
    val state = rememberTextFieldState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pad)
            .padding(16.dp)
        ,

    ) {
        Row {
            OutlinedTextField(
                state = state,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = {
                    Text("Frontend Server ip")
                }

            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
//TODO: make save functionality
                hasChanges = false
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = hasChanges
        ) {
            Text("Save")
        }
    }

}
