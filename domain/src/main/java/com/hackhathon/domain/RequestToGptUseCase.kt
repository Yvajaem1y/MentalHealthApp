package com.hackhathon.domain

import android.annotation.SuppressLint
import com.hackhathon.data.GPTRequestResult
import com.hackhathon.data.YandexGPTApiRepository
import com.hackhathon.data.models.Message
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class RequestToGptUseCase @Inject constructor(private val repository: YandexGPTApiRepository) {
    @SuppressLint("SimpleDateFormat")
    suspend operator fun invoke(user_text: String): Flow<GPTRequestResult<Message>> {
        return repository.requestToGpt(Message(sender = "user",user_text, Date()))
    }
}