package com.hackhathon.local_database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "emotion_days")
data class EmotionDayDBO(
    @PrimaryKey
    val date: Date,
    val assessments: List<Int> = emptyList(),
    val average: Double = 0.0
) {

    fun addAssessment(value: Int): EmotionDayDBO {
        require(value in 1..5) { "Value must be between 1 and 5" }
        val newAssessments = assessments + value
        val newAverage = newAssessments.average()
        return this.copy(
            assessments = newAssessments,
            average = newAverage
        )
    }

    fun removeAssessment(index: Int): EmotionDayDBO {
        val newAssessments = assessments.toMutableList().apply {
            removeAt(index)
        }
        val newAverage = if (newAssessments.isNotEmpty()) newAssessments.average() else 0.0
        return this.copy(
            assessments = newAssessments,
            average = newAverage
        )
    }
}
