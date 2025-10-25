@file:OptIn(ExperimentalMaterial3Api::class)
package com.qverzey.drawtogether.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

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
