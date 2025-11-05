package com.hackhathon.mentalhealthapp.profileScreen

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackhathon.data.RequestResults.RequestResult
import com.hackhathon.data.UserDataRepository
import com.hackhathon.data.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    internal val userDataState: StateFlow<UserDataState> =
        userDataRepository.observeUserData()
            .map { result ->
                result.toState()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserDataState.None
            )

    var name by mutableStateOf("")
    var age by mutableStateOf("")
    var gender by mutableStateOf("")

    private val _userImageUri = mutableStateOf<Uri?>(null)
    val userImageUri: State<Uri?> = _userImageUri

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            userDataState.collect { state ->
                when (state) {
                    is UserDataState.Success -> {
                        name = state.userData.userName
                        age = state.userData.userAge.toString()
                        gender = state.userData.userGender
                    }
                    is UserDataState.Loading -> {
                    }
                    is UserDataState.Error -> {
                    }
                    UserDataState.None -> {
                    }
                }
            }
        }
    }

    fun setUserImage(uri: Uri?) {
        _userImageUri.value = uri
    }

    fun clearUserImage() {
        _userImageUri.value = null
    }

    fun saveUserData() {
        viewModelScope.launch {
            userDataRepository.insertUserData(
                UserData(
                    userName = name,
                    userAge = age.toIntOrNull() ?: 0,
                    userGender = gender
                )
            )
        }
    }

    fun updateUserData() {
        viewModelScope.launch {
            viewModelScope.launch {
                userDataRepository.insertUserData(
                    UserData(
                        userName = name,
                        userAge = age.toIntOrNull() ?: 0,
                        userGender = gender
                    )
                )
            }
        }
    }
}

private fun RequestResult<UserData>.toState(): UserDataState {
    return when (this) {
        is RequestResult.Error -> UserDataState.Error(data)
        is RequestResult.InProgress -> UserDataState.Loading(data)
        is RequestResult.Success -> UserDataState.Success(data)
    }
}

internal sealed class UserDataState {
    object None : UserDataState()
    class Loading(val userData: UserData? = null) : UserDataState()
    class Error(val userData: UserData? = null) : UserDataState()
    class Success(val userData: UserData) : UserDataState()
}

internal fun UserDataState.toRequestResult(): RequestResult<UserData> {
    return when (this) {
        is UserDataState.Success -> RequestResult.Success(this.userData)
        is UserDataState.Loading -> RequestResult.InProgress(this.userData)
        is UserDataState.Error -> RequestResult.Error(this.userData)
        UserDataState.None -> RequestResult.InProgress(null)
    }
}
