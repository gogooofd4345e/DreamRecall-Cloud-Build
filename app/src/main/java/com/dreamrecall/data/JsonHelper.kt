package com.dreamrecall.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonHelper {
    
    private val gson = Gson()

    fun exportDreamsToJson(dreams: List<Dream>): String {
        return gson.toJson(dreams)
    }

    fun importDreamsFromJson(jsonString: String): List<Dream> {
        return try {
            val listType = object : TypeToken<List<Dream>>() {}.type
            gson.fromJson(jsonString, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
