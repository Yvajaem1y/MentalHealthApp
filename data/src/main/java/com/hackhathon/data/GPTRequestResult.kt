package com.hackhathon.data

sealed class GPTRequestResult<out E: Any>(open val data: E? = null) {

    class InProgress<E: Any>(data: E? = null) : GPTRequestResult<E>(data)
    class Success<E: Any>(data: E? = null) : GPTRequestResult<E>(data)
    class Error<E: Any>(override val data: E) : GPTRequestResult<E>(data)
}

fun <I: Any, O: Any> GPTRequestResult<I>.map(mapper: (I) -> O): GPTRequestResult<O> {
    return when (this) {
        is GPTRequestResult.Success<*> -> GPTRequestResult.Success(data?.let(mapper))
        is GPTRequestResult.Error<*> -> GPTRequestResult.Error(mapper(data))
        is GPTRequestResult.InProgress<*> -> GPTRequestResult.InProgress<O>(data?.let(mapper))
    }
}

internal fun <T: Any> Result<T>.toRequestResult(): GPTRequestResult<T>{
    return when{
        isSuccess -> GPTRequestResult.Success(getOrThrow())
        isFailure -> GPTRequestResult.Error(getOrThrow())
        else -> error("Impossible branch")
    }
}