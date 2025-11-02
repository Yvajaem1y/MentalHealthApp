package com.hackhathon.mentalhealthapp.addNoteScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackhathon.data.RequestResults.RequestResult
import com.hackhathon.data.models.Emotion
import com.hackhathon.data.models.Note
import com.hackhathon.domain.AddNewNoteUseCase
import com.hackhathon.domain.ObserveTodayNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val addNewNoteUseCase: Provider<AddNewNoteUseCase>,
    private val observeTodayNoteUseCase: Provider<ObserveTodayNoteUseCase>
): ViewModel(){
    private val _selectedEmotions = mutableStateListOf<Emotion>()
    val selectedEmotions: List<Emotion> get() = _selectedEmotions

    internal val todayNotesState: StateFlow<NoteState> =
        observeTodayNoteUseCase.get().invoke()
            .map { result ->
                result.toState()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = NoteState.None
            )

    var noteText by mutableStateOf("")
        private set

    fun onEmotionSelected(emotion: Emotion) {
        if (emotion.isSelected) {
            _selectedEmotions.add(emotion)
        } else {
            _selectedEmotions.removeAll { it.id == emotion.id }
        }
    }

    fun updateNoteText(text: String) {
        noteText = text
    }

    fun addNewNote(){
        viewModelScope.launch {addNewNoteUseCase.get().invoke(selectedEmotions, noteText)}
    }

    fun clearNoteText(){
        noteText = ""
    }
}

internal sealed class NoteState {
    object None : NoteState()
    data class Loading(val notes: List<Note>? = null) : NoteState()
    data class Error(val notes: List<Note>? = null) : NoteState()
    data class Success(val notes: List<Note>) : NoteState()
}

private fun RequestResult<List<Note>>.toState(): NoteState {
    return when (this) {
        is RequestResult.Error -> NoteState.Error(data)
        is RequestResult.InProgress -> NoteState.Loading(data)
        is RequestResult.Success -> NoteState.Success(data)
    }
}