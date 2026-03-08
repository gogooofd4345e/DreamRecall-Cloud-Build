package com.dreamrecall.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dreamrecall.ui.theme.BorderColor
import com.dreamrecall.ui.theme.CardBackground
import com.dreamrecall.ui.theme.GlassBackground
import com.dreamrecall.ui.theme.PurpleBlueAccent

@Composable
fun GlassOverlayPopup(
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onNext: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showDialog = true
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GlassBackground)
                    .blur(radius = 16.dp) // Glassmorphism blur effect
            ) {
                // Dimming layer
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

                AnimatedVisibility(
                    visible = showDialog,
                    enter = fadeIn(tween(400)) + scaleIn(initialScale = 0.8f, animationSpec = tween(400)),
                    exit = fadeOut(tween(300)) + scaleOut(targetScale = 0.8f),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(CardBackground)
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(listOf(BorderColor, PurpleBlueAccent.copy(alpha = 0.5f))),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(24.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = { 
                                showDialog = false
                                onDismiss() 
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                                Text("Close")
                            }
                            Button(onClick = onEdit, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = PurpleBlueAccent)) {
                                Text("Edit")
                            }
                            Button(onClick = onNext, colors = ButtonDefaults.buttonColors(containerColor = PurpleBlueAccent)) {
                                Text("Next")
                            }
                        }
                    }
                }
            }
        }
    }
}
