package com.hackhathon.data

import com.hackhathon.data.models.Message
import com.hackhathon.data.utils.getPrompt
import com.hackhathon.data.utils.toMessageDBO
import com.hackhathon.local_database.RoomDatabase
import com.hackhathon.local_database.models.EmotionDayDTO
import com.hackhathon.yandex_gpt_api.YandexGPTClient
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class YandexGPTApiRepository @Inject constructor(
    private val apiClient: YandexGPTClient,
    private val roomDatabase: RoomDatabase,
) {
    private val dao get() = roomDatabase.roomDao
    suspend fun requestToGpt(message: Message): Flow<GPTRequestResult<Message>> {
        roomDatabase.roomDao.insertMessage(message.toMessageDBO())
        val apiRequest = flow{ emit(withContext(Dispatchers.IO){apiClient.sendMessage(getPrompt(message))}.toRequestResult())}
            .map { result -> result.map{text -> Message(sender = "server", text = text, date = Date()) }}
            .also { it.map { requestResult ->
                if (requestResult is GPTRequestResult.Success) {

                    var text = requestResult.data?.text

                    if (text!=null){
                        val value = text[0].toString().toInt()
                        text = text.substring(1).trim()

                        addEmotionAssessment(value)
                        val serverMessage = Message(
                            sender = "server",
                            text = text,
                            date = Date()
                        )
                        roomDatabase.roomDao.insertMessage(serverMessage.toMessageDBO())
                    }
                    }
                }
            }

        val start = flowOf<GPTRequestResult<Message>>(GPTRequestResult.InProgress())

        return merge(apiRequest,start)
    }

    suspend fun addEmotionAssessment(value: Int, date: Date = getDateWithoutTime()) {
        require(value in 1..5) { "Value must be between 1 and 5" }

        val currentDay = dao.getEmotionDayByDate(date)
        val updatedDay = if (currentDay != null) {
            currentDay.addAssessment(value)
        } else {
            EmotionDayDTO(
                date = date,
                assessments = listOf(value),
                average = value.toDouble()
            )
        }

        dao.insertOrUpdateEmotionDay(updatedDay)
    }

    // Получение дня по дате
    suspend fun getEmotionDay(date: Date = getDateWithoutTime()): EmotionDayDTO? {
        return dao.getEmotionDayByDate(date)
    }

    // Получение всех дней за месяц
    suspend fun getMonthEmotionDays(year: Int, month: Int): List<EmotionDayDTO> {
        val calendar = Calendar.getInstance().apply {
            set(year, month, 1)
        }
        return dao.getEmotionDaysByMonth(calendar.time)
    }

    // Удаление оценки из дня
    suspend fun removeAssessment(date: Date, assessmentIndex: Int) {
        val currentDay = dao.getEmotionDayByDate(date)
        currentDay?.let {
            val updatedDay = it.removeAssessment(assessmentIndex)
            if (updatedDay.assessments.isEmpty()) {
                dao.deleteEmotionDay(date)
            } else {
                dao.insertOrUpdateEmotionDay(updatedDay)
            }
        }
    }

    // Вспомогательная функция для получения даты без времени
    private fun getDateWithoutTime(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}