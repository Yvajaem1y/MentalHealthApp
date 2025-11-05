package com.hackhathon.mentalhealthapp.mainScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private val _dates = mutableStateOf(generateDates())
    val dates = _dates

    private val _selectedDateIndex = mutableStateOf(27)
    val selectedDateIndex = _selectedDateIndex

    // Состояние диалога (сейчас не сохраняется)
    private val _showAddNoteDialog = mutableStateOf(false)
    val showAddNoteDialog = _showAddNoteDialog

    fun selectDate(index: Int) {
        _selectedDateIndex.value = index
        _dates.value = _dates.value.mapIndexed { i, dateItem ->
            dateItem.copy(isSelected = i == index)
        }
    }

    fun showAddNoteDialog() {
        _showAddNoteDialog.value = true
    }

    fun hideAddNoteDialog() {
        _showAddNoteDialog.value = false
    }

    private fun generateDates(): List<DateItem> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("d", Locale.getDefault())
        val dayFormat = SimpleDateFormat("E", Locale.getDefault())

        val totalDays = 61
        val todayIndex = 30

        calendar.add(Calendar.DAY_OF_YEAR, -30)

        return List(totalDays) { index ->
            val currentDate = calendar.time
            val date = dateFormat.format(currentDate)
            val dayOfWeek = formatDayOfWeek(dayFormat.format(currentDate))

            val today = Calendar.getInstance()
            val isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)

            val isSelected = index == todayIndex

            val result = DateItem(
                date = date,
                dayOfWeek = dayOfWeek,
                fullDate = calendar.clone() as Calendar,
                isSelected = isSelected,
                isToday = isToday
            )

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            result
        }
    }

    private fun formatDayOfWeek(day: String): String = when (day) {
        "Mon" -> "Пн"
        "Tue" -> "Вт"
        "Wed" -> "Ср"
        "Thu" -> "Чт"
        "Fri" -> "Пт"
        "Sat" -> "Сб"
        "Sun" -> "Вс"
        else -> day.take(2)
    }
}
