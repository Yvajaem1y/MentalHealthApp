package com.hackhathon.mentalhealthapp.techniqueSelectionScreen.utils

import com.hackhathon.data.models.BreathingTechniquesData
import com.hackhathon.data.models.Emotion
import com.hackhathon.local_database.models.EmotionDBO
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingTechnique
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingTechniques

internal fun BreathingTechnique.toData(): BreathingTechniquesData {
    return BreathingTechniquesData(
        id = id
    )
}

internal fun BreathingTechniquesData.toUIData(): BreathingTechnique {
    return BreathingTechniques.techniques.find { it.id == id }
        ?: throw IllegalArgumentException("Technique with id $id not found")
}