package com.hackhathon.mentalhealthapp.mainScreen

import java.util.Calendar

data class DateItem(
    val date: String,
    val dayOfWeek: String,
    val fullDate: Calendar,
    val isSelected: Boolean = false,
    val isToday: Boolean = false
)