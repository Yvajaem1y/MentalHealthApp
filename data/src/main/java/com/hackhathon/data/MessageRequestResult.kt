package com.hackhathon.data

sealed class MessageRequestResult<out E: Any>(open val data: E? = null) {

    class InProgress<E: Any>(data: E? = null) : MessageRequestResult<E>(data)
    class Success<E: Any>(override val data: E) : MessageRequestResult<E>(data)
    class Error<E: Any>(data: E? = null, val error: Throwable? = null) : MessageRequestResult<E>(data)
}

fun <I: Any, O: Any> MessageRequestResult<I>.map(mapper: (I) -> O): MessageRequestResult<O> {
    return when (this) {
        is MessageRequestResult.Success -> MessageRequestResult.Success(mapper(data))
        is MessageRequestResult.Error<*> -> MessageRequestResult.Error(data?.let(mapper))
        is MessageRequestResult.InProgress<*> -> MessageRequestResult.InProgress<O>(data?.let(mapper))
    }
}

internal fun <T: Any> Result<T>.toMessageRequestResult(): MessageRequestResult<T>{
    return when{
        isSuccess -> MessageRequestResult.Success(getOrThrow())
        isFailure -> MessageRequestResult.Error()
        else -> error("Impossible branch")
    }
}