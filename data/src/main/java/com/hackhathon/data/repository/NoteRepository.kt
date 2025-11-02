package com.hackhathon.data.repository

import com.hackhathon.data.RequestResults.RequestResult
import com.hackhathon.data.models.Note
import com.hackhathon.data.utils.toNote
import com.hackhathon.data.utils.toNoteDBO
import com.hackhathon.local_database.RoomDatabase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Date

class NoteRepository @Inject constructor(
    private val roomDatabase: RoomDatabase,
){
    suspend fun addNewNote(note: Note){
        roomDatabase.roomDao.insertOrUpdateNote(note.toNoteDBO())
    }

    fun observeNotesByDate(targetDate: Date): Flow<RequestResult<List<Note>>> {
        return flow {
            emit(RequestResult.InProgress())

            try {
                val (startOfDay, endOfDay) = getDateRange(targetDate)

                val initialNotes = roomDatabase.roomDao.getAllNotes()
                    .filter { noteEntity ->
                        val noteDate = noteEntity.toNote().date
                        noteDate >= startOfDay && noteDate < endOfDay
                    }
                    .map { it.toNote() }

                emit(RequestResult.Success(initialNotes))
            } catch (e: Exception) {
                emit(RequestResult.Error<List<Note>>(error = e))
                return@flow
            }

            roomDatabase.roomDao.observeAllNotes()
                .map { notes ->
                    val (startOfDay, endOfDay) = getDateRange(targetDate)
                    notes.filter { noteEntity ->
                        val noteDate = noteEntity.toNote().date
                        noteDate >= startOfDay && noteDate < endOfDay
                    }.map { it.toNote() }
                }
                .collect { filteredNotes ->
                    emit(RequestResult.Success(filteredNotes))
                }
        }
            .catch { error ->
                emit(RequestResult.Error<List<Note>>(error = error))
            }
    }

    private fun getDateRange(date: Date): Pair<Date, Date> {
        val calendar = Calendar.getInstance().apply {
            time = date
        }

        val startOfDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val endOfDay = calendar.apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }.time

        return Pair(startOfDay, endOfDay)
    }
}