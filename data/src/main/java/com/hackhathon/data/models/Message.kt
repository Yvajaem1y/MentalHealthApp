package com.hackhathon.data.models

import java.text.SimpleDateFormat
import java.util.Date

data class Message(
    val sender: String,
    val text: String,
    val date: Date
)
