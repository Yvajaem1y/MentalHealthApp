package com.hackhathon.local_database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hackhathon.local_database.models.EmotionDBO
import java.util.Date

internal class Converters {

    // Исправленные конвертеры для Date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    private val gson = Gson()

    // Конвертеры для List<Int>
    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toIntList(data: String): List<Int> {
        if (data.isBlank()) return emptyList()
        return try {
            val listType = object : TypeToken<List<Int>>() {}.type
            gson.fromJson(data, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Конвертеры для List<EmotionDBO>
    @TypeConverter
    fun fromEmotionList(emotions: List<EmotionDBO>): String {
        return gson.toJson(emotions)
    }

    @TypeConverter
    fun toEmotionList(emotionsString: String): List<EmotionDBO> {
        if (emotionsString.isBlank()) return emptyList()
        return try {
            val listType = object : TypeToken<List<EmotionDBO>>() {}.type
            gson.fromJson(emotionsString, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}