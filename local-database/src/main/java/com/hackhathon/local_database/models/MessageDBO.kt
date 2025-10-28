package com.hackhathon.local_database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity(tableName = "messages")
class MessageDBO (
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo("sender") val senderDBO: String,
    @ColumnInfo("text") val textDBO: String,
    @ColumnInfo("date") val dateDBO: String
)