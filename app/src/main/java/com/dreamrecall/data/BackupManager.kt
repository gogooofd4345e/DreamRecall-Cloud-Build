package com.dreamrecall.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class BackupManager(private val context: Context, private val repository: DreamRepository) {

    suspend fun exportDataToFile(uri: Uri, dreams: List<Dream>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val jsonStr = JsonHelper.exportDreamsToJson(dreams)
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        writer.write(jsonStr)
                    }
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun importDataFromFile(uri: Uri): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val jsonStr = StringBuilder()
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line: String? = reader.readLine()
                        while (line != null) {
                            jsonStr.append(line)
                            line = reader.readLine()
                        }
                    }
                }
                
                val importedDreams = JsonHelper.importDreamsFromJson(jsonStr.toString())
                if (importedDreams.isNotEmpty()) {
                    importedDreams.forEach { repository.insert(it) }
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
