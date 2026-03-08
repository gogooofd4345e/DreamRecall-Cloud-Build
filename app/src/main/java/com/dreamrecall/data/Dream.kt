package com.dreamrecall.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "dreams")
@TypeConverters(Converters::class)
data class Dream(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val dateAdded: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList(),
    val lastShownMillis: Long = 0L // To avoid showing the same dream consecutively
)

// Converters for List<String> and Date
class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}
