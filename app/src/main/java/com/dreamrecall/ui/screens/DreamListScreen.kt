package com.dreamrecall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dreamrecall.ui.components.DreamCard
import com.dreamrecall.ui.components.DreamInputField
import com.dreamrecall.ui.theme.BlackBackground
import com.dreamrecall.ui.theme.PurpleBlueAccent

// Temporary Mock Data Model for UI Phase
data class MockDream(val id: Int, val title: String, val body: String, val date: String, val tags: List<String>)

@Composable
fun DreamListScreen(
    dreams: List<MockDream>,
    onAddClick: () -> Unit,
    onDreamClick: (MockDream) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BlackBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PurpleBlueAccent,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Dream", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(text = "DreamRecall", style = MaterialTheme.typography.headlineMedium)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            DreamInputField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Search dreams or tags..."
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val filtered = dreams.filter { 
                    it.title.contains(searchQuery, true) || 
                    it.body.contains(searchQuery, true) || 
                    it.tags.any { tag -> tag.contains(searchQuery, true) } 
                }
                
                items(filtered.size) { index ->
                    val dream = filtered[index]
                    DreamCard(
                        title = dream.title,
                        snippet = dream.body,
                        date = dream.date,
                        tags = dream.tags,
                        onClick = { onDreamClick(dream) },
                        delayMs = index * 100 // Staggered entrance animation
                    )
                }
            }
        }
    }
}
