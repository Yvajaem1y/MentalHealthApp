package com.hackhathon.local_database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.DateFormat
import java.util.Date

internal class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { DateFormat.getDateTimeInstance().parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.time?.let { DateFormat.getDateTimeInstance().format(it) }
    }

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<Int>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(data: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(data, listType) ?: emptyList()
    }
}