package com.hackhathon.data.models

import java.util.Date

data class Note (
    val date: Date,
    val emotions : List<Emotion>,
    val noteText : String,
)