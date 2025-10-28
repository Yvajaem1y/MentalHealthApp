package com.hackhathon.local_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.hackhathon.local_database.models.EmotionDayDTO
import com.hackhathon.local_database.models.MessageDBO
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface RoomDAO {
    @Query("SELECT * FROM messages")
    suspend fun getAllMessages(): List<MessageDBO>

    @Query("SELECT * FROM messages")
    fun observeAllMessages(): Flow<List<MessageDBO>>

    @Insert(onConflict = REPLACE)
    suspend fun insertMessage(messageDBO: MessageDBO)

    // Работа с эмоциональными днями
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateEmotionDay(emotionDay: EmotionDayDTO)

    @Query("SELECT * FROM emotion_days WHERE date = :date")
    suspend fun getEmotionDayByDate(date: Date): EmotionDayDTO?

    @Query("SELECT * FROM emotion_days WHERE strftime('%Y-%m', date/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch')")
    suspend fun getEmotionDaysByMonth(date: Date): List<EmotionDayDTO>

    @Query("SELECT * FROM emotion_days WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getEmotionDaysInRange(startDate: Date, endDate: Date): List<EmotionDayDTO>

    @Query("DELETE FROM emotion_days WHERE date = :date")
    suspend fun deleteEmotionDay(date: Date)
}