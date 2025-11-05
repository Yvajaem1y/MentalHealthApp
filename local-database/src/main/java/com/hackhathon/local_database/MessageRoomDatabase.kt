package com.hackhathon.local_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hackhathon.local_database.dao.RoomDAO
import com.hackhathon.local_database.models.BreathingTechniquesDBO
import com.hackhathon.local_database.models.EmotionDayDBO
import com.hackhathon.local_database.models.MessageDBO
import com.hackhathon.local_database.models.NoteDBO
import com.hackhathon.local_database.models.UserDataDBO

class RoomDatabase internal constructor(private val database: MessageRoomDatabase){
    val roomDao: RoomDAO
        get() =database.messagesDAO()
}

@Database(entities = [MessageDBO::class, EmotionDayDBO::class, NoteDBO::class, BreathingTechniquesDBO::class, UserDataDBO::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class MessageRoomDatabase : RoomDatabase(){
    abstract fun messagesDAO(): RoomDAO
}

fun RoomDatabase(applicationContext: Context): com.hackhathon.local_database.RoomDatabase {
    val messageDao = Room.databaseBuilder(
        context = checkNotNull(applicationContext.applicationContext),
        klass= MessageRoomDatabase::class.java,
        name = "RoomDatabase"
    ).build()
    return RoomDatabase(messageDao)
}