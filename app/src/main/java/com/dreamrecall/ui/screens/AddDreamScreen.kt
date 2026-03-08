package com.dreamrecall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamrecall.ui.components.BounceButton
import com.dreamrecall.ui.components.DreamInputField
import com.dreamrecall.ui.theme.BlackBackground

@Composable
fun AddDreamScreen(
    onSaveDream: (title: String, body: String, tags: String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BlackBackground
    ) { paddingVals ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVals)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(text = "Log a New Dream", style = MaterialTheme.typography.headlineMedium)
            
            Spacer(modifier = Modifier.height(32.dp))

            DreamInputField(
                value = title,
                onValueChange = { title = it },
                placeholder = "Dream Title"
            )

            Spacer(modifier = Modifier.height(16.dp))

            DreamInputField(
                value = body,
                onValueChange = { body = it },
                placeholder = "What happened? Describe vividly...",
                singleLine = false,
                modifier = Modifier.weight(1f) // Takes up remaining space
            )

            Spacer(modifier = Modifier.height(16.dp))

            DreamInputField(
                value = tags,
                onValueChange = { tags = it },
                placeholder = "Tags (comma separated, e.g. lucid, nightmare)"
            )

            Spacer(modifier = Modifier.height(32.dp))

            BounceButton(
                text = "Save Dream",
                onClick = { onSaveDream(title, body, tags) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
