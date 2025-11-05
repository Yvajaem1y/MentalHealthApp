package com.hackhathon.local_database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class UserDataDBO (
    @PrimaryKey val id : Int = 0,
    val userName : String,
    val userAge : Int,
    val userGender : String
)