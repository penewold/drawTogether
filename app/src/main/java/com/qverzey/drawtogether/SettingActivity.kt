@file:OptIn(ExperimentalMaterial3Api::class)

package com.qverzey.drawtogether

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun SettingScreen(pad: PaddingValues = PaddingValues.Zero) {
    var hasChanges by remember { mutableStateOf(false) }
    var serverIpState = rememberTextFieldState("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pad)
            .padding(16.dp)
        ,

    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                state = serverIpState,
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
