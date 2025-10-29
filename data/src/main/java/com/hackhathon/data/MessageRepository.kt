package com.hackhathon.data

import com.hackhathon.data.models.Message
import com.hackhathon.data.utils.toMessage
import com.hackhathon.local_database.RoomDatabase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch

class MessageRepository @Inject constructor(
    private val roomDatabase: RoomDatabase,
){

    fun observeMessage(): Flow<MessageRequestResult<List<Message>>> {
        return flow {
            emit(MessageRequestResult.InProgress())

            try {
                val initialMessages = roomDatabase.roomDao.getAllMessages()
                val convertedInitialMessages = initialMessages.map { it.toMessage() }
                emit(MessageRequestResult.Success(convertedInitialMessages))
            } catch (e: Exception) {
                emit(MessageRequestResult.Error<List<Message>>(error = e))
                return@flow
            }

            roomDatabase.roomDao.observeAllMessages()
                .collect { messages ->
                    val convertedMessages = messages.map { it.toMessage() }
                    emit(MessageRequestResult.Success(convertedMessages))
                }
        }
            .catch { error ->
                emit(MessageRequestResult.Error<List<Message>>(error = error))
            }
    }
}