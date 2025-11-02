package com.hackhathon.domain

import com.hackhathon.data.RequestResults.RequestResult
import com.hackhathon.data.models.Emotion
import com.hackhathon.data.models.Note
import com.hackhathon.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class AddNewNoteUseCase @Inject constructor(private val repository: NoteRepository){
    suspend fun invoke(emotions: List<Emotion>, noteText: String) {
        val selectedEmotions = emotions.filter { it.isSelected }

        repository.addNewNote(Note(Date(),selectedEmotions,noteText))
    }
}