package com.hackhathon.yandex_gpt_api

import com.hackhathon.yandex_gpt_api.models.YandexChatCompletionRequest
import com.hackhathon.yandex_gpt_api.models.YandexChatCompletionResponse
import com.hackhathon.yandex_gpt_api.models.YandexMessage
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface YandexApiService {
    @POST("foundationModels/v1/completion")
    suspend fun sendMessage(
        @Header("Authorization") authorization: String,
        @Header("x-folder-id") folderId: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: YandexChatCompletionRequest
    ): YandexChatCompletionResponse
}

fun createYandexApiService(
    baseUrl: String = "https://llm.api.cloud.yandex.net",
    json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
        isLenient = true
    }
): YandexApiService {
    return createRetrofit(baseUrl, json).create(YandexApiService::class.java)
}

private fun createRetrofit(
    baseUrl: String,
    json: Json
): Retrofit {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .client(client)
        .build()
}

class YandexGPTClient(
    private val apiKey: String,
    private val folderId: String,
    private val useIAMToken: Boolean = false
) {
    private val service = createYandexApiService()

    private fun getAuthorizationHeader(): String {
        return if (useIAMToken) {
            "Bearer $apiKey"
        } else {
            "Api-Key $apiKey"
        }
    }

    suspend fun sendMessage(
        message: String,
        model: String = "yandexgpt",
        systemPrompt: String? = null
    ): Result<String> {
        return try {
            val messages = mutableListOf<YandexMessage>()

            // Добавляем системный промпт если указан
            systemPrompt?.let {
                messages.add(YandexMessage(role = "system", text = it))
            }

            // Добавляем пользовательское сообщение
            messages.add(YandexMessage(role = "user", text = message))

            val request = YandexChatCompletionRequest(
                modelUri = "gpt://$folderId/$model",
                completionOptions = YandexChatCompletionRequest.CompletionOptions(
                    stream = false,
                    temperature = 0.7,
                    maxTokens = 2000
                ),
                messages = messages
            )

            val response = service.sendMessage(
                authorization = getAuthorizationHeader(),
                folderId = folderId,
                request = request
            )

            Result.success(response.result.alternatives.firstOrNull()?.message?.text ?: "No response received")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()

            val errorMessage = when (e.code()) {
                400 -> "Bad Request: Проверьте параметры запроса. $errorBody"
                401 -> "Unauthorized: Проверьте API ключ или IAM токен. Убедитесь что используете правильный тип авторизации."
                403 -> "Forbidden: Проверьте folderId и права доступа. FolderId: $folderId"
                429 -> "Rate Limit Exceeded"
                500 -> "Internal Server Error"
                503 -> "Service Unavailable"
                else -> "HTTP ${e.code()}: $errorBody"
            }
            Result.failure(RuntimeException(errorMessage))
        } catch (e: Exception) {
            println("Debug: General exception: ${e.message}")
            Result.failure(e)
        }
    }
}