package com.hackhathon.mentalhealthapp.chatGptScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackhathon.data.RequestResults.GPTRequestResult
import com.hackhathon.data.RequestResults.RequestResult
import com.hackhathon.data.models.Message
import com.hackhathon.domain.ObserveTodayMessageUseCase
import com.hackhathon.domain.RequestToGptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class GptViewModel @Inject constructor(
    private val requestToGptUseCase: Provider<RequestToGptUseCase>,
    private val observeTodayMessageUseCase: Provider<ObserveTodayMessageUseCase>
): ViewModel() {

    private val _GptRequest_stateRequestToGpt = MutableStateFlow<GptRequestState>(GptRequestState.None)
    internal val gptRequestStateRequestToGpt: StateFlow<GptRequestState> = _GptRequest_stateRequestToGpt.asStateFlow()

    internal val messagesGptRequestState: StateFlow<MessageState> =
        observeTodayMessageUseCase.get().invoke()
            .map { result ->
                result.toState()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MessageState.None
            )

    internal fun requestToGpt(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            requestToGptUseCase.get().invoke(text)
                .collect { result ->
                    _GptRequest_stateRequestToGpt.value = result.toState()
                }
        }
    }
}

private fun GPTRequestResult<Message>.toState(): GptRequestState {
    return when (this) {
        is GPTRequestResult.Error -> GptRequestState.Error(data)
        is GPTRequestResult.InProgress -> GptRequestState.Loading()
        is GPTRequestResult.Success -> GptRequestState.Success()
    }
}

private fun RequestResult<List<Message>>.toState(): MessageState {
    return when (this) {
        is RequestResult.Error -> MessageState.Error(data)
        is RequestResult.InProgress -> MessageState.Loading(data)
        is RequestResult.Success -> MessageState.Success(data)
    }
}

internal sealed class GptRequestState {

    object None : GptRequestState()

    class Loading() : GptRequestState()
    class Error(val message: Message? = null) : GptRequestState()
    class Success() : GptRequestState()
}

internal sealed class MessageState {

    object None : MessageState()

    class Loading(val messages: List<Message>? = null) : MessageState()
    class Error(val messages: List<Message>? = null) : MessageState()
    class Success(val messages: List<Message>) : MessageState()
}