package com.dreamrecall

import android.app.Application
import com.dreamrecall.data.BackupManager
import com.dreamrecall.data.DreamDatabase
import com.dreamrecall.data.DreamRepository

class DreamRecallApplication : Application() {
    
    val database by lazy { DreamDatabase.getDatabase(this) }
    val repository by lazy { DreamRepository(database.dreamDao()) }
    val backupManager by lazy { BackupManager(this, repository) }
    
    override fun onCreate() {
        super.onCreate()
        // Initialization code if needed
    }
}
