package com.hackhathon.domain

import com.hackhathon.data.MessageRepository
import com.hackhathon.data.RequestResults.RequestResult
import com.hackhathon.data.models.Message
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class ObserveTodayMessageUseCase @Inject constructor(private val repository: MessageRepository){
    fun invoke() : Flow<RequestResult<List<Message>>>
    {
        return repository.observeMessagesByDate(Date())
    }
}