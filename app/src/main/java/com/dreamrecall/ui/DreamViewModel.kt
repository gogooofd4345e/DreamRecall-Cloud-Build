package com.dreamrecall.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dreamrecall.data.BackupManager
import com.dreamrecall.data.Dream
import com.dreamrecall.data.DreamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DreamViewModel(
    private val repository: DreamRepository,
    private val backupManager: BackupManager
) : ViewModel() {

    private val _dreams = MutableStateFlow<List<Dream>>(emptyList())
    val dreams: StateFlow<List<Dream>> = _dreams.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        // Collect all dreams by default
        viewModelScope.launch {
            repository.allDreams.collect {
                _dreams.value = it
            }
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            if (query.isBlank()) {
                repository.allDreams.collect { _dreams.value = it }
            } else {
                repository.searchDreams(query).collect { _dreams.value = it }
            }
        }
    }

    fun addDream(title: String, body: String, tagsStr: String) {
        val tagsList = tagsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        val newDream = Dream(title = title, body = body, tags = tagsList)
        viewModelScope.launch {
            repository.insert(newDream)
        }
    }

    fun deleteDream(dream: Dream) {
        viewModelScope.launch {
            repository.delete(dream)
        }
    }

    // Export functionality
    fun exportDreams(uri: Uri, context: Context, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val currentDreams = repository.allDreams.first()
            val result = backupManager.exportDataToFile(uri, currentDreams)
            onResult(result)
        }
    }

    // Import functionality
    fun importDreams(uri: Uri, context: Context, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = backupManager.importDataFromFile(uri)
            onResult(result)
        }
    }
}

// ViewModel Factory
class DreamViewModelFactory(
    private val repository: DreamRepository,
    private val backupManager: BackupManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DreamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DreamViewModel(repository, backupManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
