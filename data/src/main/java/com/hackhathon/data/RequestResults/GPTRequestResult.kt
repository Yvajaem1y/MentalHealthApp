package com.hackhathon.data.RequestResults

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

sealed class RequestResult<out E: Any>(open val data: E? = null) {

    class InProgress<E: Any>(data: E? = null) : RequestResult<E>(data)
    class Success<E: Any>(override val data: E) : RequestResult<E>(data)
    class Error<E: Any>(data: E? = null, val error: Throwable? = null) : RequestResult<E>(data)
}

fun <I: Any, O: Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(mapper(data))
        is RequestResult.Error<*> -> RequestResult.Error(data?.let(mapper))
        is RequestResult.InProgress<*> -> RequestResult.InProgress<O>(data?.let(mapper))
    }
}

internal fun <T: Any> Result<T>.toMessageRequestResult(): RequestResult<T>{
    return when{
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> error("Impossible branch")
    }
}