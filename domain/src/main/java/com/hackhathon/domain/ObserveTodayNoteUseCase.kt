package com.hackhathon.domain

import com.hackhathon.data.RequestResults.RequestResult
import com.hackhathon.data.models.Note
import com.hackhathon.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class ObserveTodayNoteUseCase @Inject constructor(private val repository: NoteRepository){
    fun invoke() : Flow<RequestResult<List<Note>>>
    {
        return repository.observeNotesByDate(Date())
    }
}