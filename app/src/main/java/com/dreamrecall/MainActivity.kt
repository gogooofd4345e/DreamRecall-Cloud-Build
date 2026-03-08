package com.dreamrecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dreamrecall.data.Dream
import com.dreamrecall.ui.DreamViewModel
import com.dreamrecall.ui.DreamViewModelFactory
import com.dreamrecall.ui.components.GlassOverlayPopup
import com.dreamrecall.ui.screens.AddDreamScreen
import com.dreamrecall.ui.screens.DreamListScreen
import com.dreamrecall.ui.screens.MockDream
import com.dreamrecall.ui.theme.BlackBackground
import com.dreamrecall.ui.theme.DreamRecallTheme
import com.dreamrecall.worker.WorkScheduler

class MainActivity : ComponentActivity() {

    private val viewModel: DreamViewModel by viewModels {
        DreamViewModelFactory(
            (application as DreamRecallApplication).repository,
            (application as DreamRecallApplication).backupManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schedule random dream reminders (e.g., every 60 minutes)
        WorkScheduler.scheduleReminders(this, 60L)

        // Check if opened from notification deep link
        val dreamIdFromNotification = intent.getIntExtra("SHOW_DREAM_ID", -1)

        setContent {
            DreamRecallTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BlackBackground
                ) {
                    DreamRecallApp(viewModel, dreamIdFromNotification)
                }
            }
        }
    }
}

@Composable
fun DreamRecallApp(viewModel: DreamViewModel, initialDreamId: Int) {
    val navController = rememberNavController()
    val dreams by viewModel.dreams.collectAsState()
    var showPopupForDream by remember { mutableStateOf<Dream?>(null) }

    // If launched from notification, immediately show the popup for that dream
    if (initialDreamId != -1 && showPopupForDream == null) {
        val targetDream = dreams.find { it.id == initialDreamId }
        targetDream?.let { showPopupForDream = it }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            // Map real Room dreams to UI MockDreams for rendering
            val uiDreams = dreams.map { 
                MockDream(it.id, it.title, it.body, java.text.DateFormat.getDateInstance().format(java.util.Date(it.dateAdded)), it.tags) 
            }

            DreamListScreen(
                dreams = uiDreams,
                onAddClick = { navController.navigate("add_dream") },
                onDreamClick = { clickedDream ->
                    showPopupForDream = dreams.find { it.id == clickedDream.id }
                }
            )
        }
        composable("add_dream") {
            AddDreamScreen(
                onSaveDream = { title, body, tags ->
                    viewModel.addDream(title, body, tags)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        // Note: Export/Import flows would typically be exposed via buttons in a Settings screen
    }

    // Glass Overlay Popup Layer
    showPopupForDream?.let { dream ->
        GlassOverlayPopup(
            title = dream.title,
            text = dream.body,
            onDismiss = { showPopupForDream = null },
            onEdit = { 
                showPopupForDream = null
                // Future expansion: Navigate to edit screen with dream.id
            },
            onNext = {
                // Future expansion: Select a different random dream
                showPopupForDream = dreams.randomOrNull()
            }
        )
    }
}
