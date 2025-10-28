// models.kt
package com.hackhathon.yandex_gpt_api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YandexChatCompletionRequest(
    @SerialName("modelUri")
    val modelUri: String,

    @SerialName("completionOptions")
    val completionOptions: CompletionOptions,

    @SerialName("messages")
    val messages: List<YandexMessage>
) {
    @Serializable
    data class CompletionOptions(
        @SerialName("stream")
        val stream: Boolean = false,

        @SerialName("temperature")
        val temperature: Double? = null,

        @SerialName("maxTokens")
        val maxTokens: Int? = null
    )
}

@Serializable
data class YandexMessage(
    @SerialName("role")
    val role: String, // "system", "user", "assistant"

    @SerialName("text")
    val text: String
)

@Serializable
data class YandexChatCompletionResponse(
    @SerialName("result")
    val result: Result
) {
    @Serializable
    data class Result(
        @SerialName("alternatives")
        val alternatives: List<Alternative>,

        @SerialName("usage")
        val usage: Usage,

        @SerialName("modelVersion")
        val modelVersion: String
    )

    @Serializable
    data class Alternative(
        @SerialName("message")
        val message: YandexMessage,

        @SerialName("status")
        val status: String
    )

    @Serializable
    data class Usage(
        @SerialName("inputTextTokens")
        val inputTextTokens: String,

        @SerialName("completionTokens")
        val completionTokens: String,

        @SerialName("totalTokens")
        val totalTokens: String
    )
}

@Serializable
data class YandexApiError(
    @SerialName("error")
    val error: ErrorDetail
) {
    @Serializable
    data class ErrorDetail(
        @SerialName("message")
        val message: String,

        @SerialName("code")
        val code: Int,

        @SerialName("grpcCode")
        val grpcCode: Int? = null
    )
}