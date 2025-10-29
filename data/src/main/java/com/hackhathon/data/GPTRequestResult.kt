package com.hackhathon.data

sealed class GPTRequestResult<out E: Any>(open val data: E? = null) {
    class InProgress<E: Any>(data: E? = null) : GPTRequestResult<E>(data)
    class Success<E: Any>(data: E? = null) : GPTRequestResult<E>(data)
    class Error<E: Any>(val message: String, data: E? = null) : GPTRequestResult<E>(data) // Добавлено поле message
}

fun <I: Any, O: Any> GPTRequestResult<I>.map(mapper: (I) -> O): GPTRequestResult<O> {
    return when (this) {
        is GPTRequestResult.Success -> {
            if (data != null) {
                GPTRequestResult.Success(mapper(data))
            } else {
                GPTRequestResult.Success(null)
            }
        }
        is GPTRequestResult.Error -> {
            GPTRequestResult.Error(
                message = this.message,
                data = data?.let(mapper)
            )
        }
        is GPTRequestResult.InProgress -> {
            GPTRequestResult.InProgress(data?.let(mapper))
        }
    }
}

internal fun <T: Any> Result<T>.toGptRequestResult(): GPTRequestResult<T> {
    return try {
        GPTRequestResult.Success(getOrThrow())
    } catch (e: Exception) {
        GPTRequestResult.Error(e.message ?: "Unknown error", null)
    }
}