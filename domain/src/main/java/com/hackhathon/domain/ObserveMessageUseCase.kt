package com.hackhathon.domain

import com.hackhathon.data.MessageRepository
import com.hackhathon.data.MessageRequestResult
import com.hackhathon.data.YandexGPTApiRepository
import com.hackhathon.data.models.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessageUseCase @Inject constructor(private val repository: MessageRepository){
    fun invoke() : Flow<MessageRequestResult<List<Message>>>
    {
        return repository.observeMessage()
    }
}