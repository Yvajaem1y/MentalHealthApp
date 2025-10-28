package com.hackhathon.mentalhealthapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackhathon.data.GPTRequestResult
import com.hackhathon.data.models.Message
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
class MainActivityViewModel @Inject constructor(
    private val requestToGptUseCase: Provider<RequestToGptUseCase>
): ViewModel(){

    private val _stateRequestToGpt = MutableStateFlow<State>(State.None)
    internal val stateRequestToGpt: StateFlow<State> = _stateRequestToGpt.asStateFlow()

    fun requestToGpt(text : String){
        viewModelScope.launch(Dispatchers.Default) {
            requestToGptUseCase.get().invoke(text)
                .map { it.toState() }
                .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
                .collect { _stateRequestToGpt.value=it }
        }

    }

}

private fun GPTRequestResult<Message>.toState(): State {
    return when (this) {
        is GPTRequestResult.Error -> State.Error(data)
        is GPTRequestResult.InProgress -> State.Loading()
        is GPTRequestResult.Success -> State.Success()
    }
}

internal sealed class State {

    object None : State()

    class Loading() : State()
    class Error(val articleUIS: Message) : State()
    class Success() : State()
}