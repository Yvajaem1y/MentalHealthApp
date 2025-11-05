package com.hackhathon.local_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.hackhathon.local_database.models.BreathingTechniquesDBO
import com.hackhathon.local_database.models.EmotionDayDBO
import com.hackhathon.local_database.models.MessageDBO
import com.hackhathon.local_database.models.NoteDBO
import com.hackhathon.local_database.models.UserDataDBO
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

    @Insert(onConflict = REPLACE)
    suspend fun insertFavoriteTechnique(breathingTechniquesDBO: BreathingTechniquesDBO)

    @Insert(onConflict = REPLACE)
    suspend fun insertUserData(userDataDBO: UserDataDBO)

    @Query("SELECT * FROM user_data_table")
    fun observeUserData(): Flow<List<UserDataDBO>>

    @Query("DELETE FROM favorite_technique_table WHERE id = :techniqueId")
    suspend fun deleteFavoriteTechnique(techniqueId: Int)

    @Query("SELECT * FROM favorite_technique_table")
    fun observeFavoriteTechnique(): Flow<List<BreathingTechniquesDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateEmotionDay(emotionDay: EmotionDayDBO)

    @Query("SELECT * FROM emotion_days WHERE date = :date")
    suspend fun getEmotionDayByDate(date: Date): EmotionDayDBO?

    @Query("SELECT * FROM emotion_days WHERE strftime('%Y-%m', date/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch')")
    suspend fun getEmotionDaysByMonth(date: Date): List<EmotionDayDBO>

    @Query("SELECT * FROM emotion_days WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getEmotionDaysInRange(startDate: Date, endDate: Date): List<EmotionDayDBO>

    @Query("DELETE FROM emotion_days WHERE date = :date")
    suspend fun deleteEmotionDay(date: Date)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: NoteDBO)

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<NoteDBO>

    @Query("SELECT * FROM notes")
    fun observeAllNotes(): Flow<List<NoteDBO>>
}