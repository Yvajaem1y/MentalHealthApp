package com.hackhathon.local_database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hackhathon.local_database.Converters
import java.util.Date

@Entity(tableName = "notes")
data class NoteDBO(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    @TypeConverters(Converters::class)
    val emotions: List<EmotionDBO>,
    val noteText: String
)
