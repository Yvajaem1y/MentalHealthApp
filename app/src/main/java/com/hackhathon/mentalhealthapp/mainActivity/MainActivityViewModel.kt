package com.hackhathon.mentalhealthapp.mainActivity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(){
    var currentScreen: MutableState<Screen> = mutableStateOf<Screen>(Screen.ScreenBNav)

    fun setCurrentScreen(screen : Screen){
        currentScreen.value=screen
    }
}

sealed class Screen {
    object StartScreen : Screen()
    object ScreenBNav : Screen()
    object ScreenChatGpt : Screen()
    object ScreenBreathing : Screen()
    object ScreenChooseDifficulty : Screen()
}