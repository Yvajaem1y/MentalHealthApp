package com.hackhathon.mentalhealthapp.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackhathon.data.models.UserData
import com.hackhathon.mentalhealthapp.addNoteScreen.NoteViewModel
import com.hackhathon.mentalhealthapp.bottomNav.BottomNavViewModel
import com.hackhathon.mentalhealthapp.bottomNav.ScreenWithBottomNav
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingScreen
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingViewModel
import com.hackhathon.mentalhealthapp.chatGptScreen.ChatWithGptScreen
import com.hackhathon.mentalhealthapp.chatGptScreen.GptViewModel
import com.hackhathon.mentalhealthapp.chooseDifficultyScreen.ChooseDifficultyScreen
import com.hackhathon.mentalhealthapp.mainScreen.MainScreenViewModel
import com.hackhathon.mentalhealthapp.profileScreen.ProfileViewModel
import com.hackhathon.mentalhealthapp.profileScreen.UserDataState
import com.hackhathon.mentalhealthapp.startScreen.StartScreen
import com.hackhathon.mentalhealthapp.techniqueSelectionScreen.TechniqueSelectionScreenViewModel
import com.hackhathon.ui_kit.theme.MentalHealthAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MentalHealthAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navigationViewModel: MainViewModel = viewModel()
    val gptViewModel: GptViewModel = viewModel()
    val breathingViewModel: BreathingViewModel = viewModel()
    val noteViewModel: NoteViewModel = viewModel()
    val mainScreenViewModel: MainScreenViewModel = viewModel()
    val bottomNavViewModel: BottomNavViewModel = viewModel()
    val techniqueSelectionScreenViewModel: TechniqueSelectionScreenViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    val currentScreen by navigationViewModel.currentScreen

    val userDataState by profileViewModel.userDataState.collectAsStateWithLifecycle()

    LaunchedEffect(userDataState) {
        when (userDataState) {
            is UserDataState.Success -> {
                val userData = (userDataState as UserDataState.Success).userData
                if (userData.userName.isBlank() && currentScreen != Screen.StartScreen) {
                    navigationViewModel.setCurrentScreen(Screen.StartScreen)
                } else if (userData.userName.isNotBlank() && currentScreen == Screen.StartScreen) {
                    navigationViewModel.setCurrentScreen(Screen.ScreenBNav)
                }
            }
            is UserDataState.Error -> {
                if (currentScreen != Screen.StartScreen) {
                    navigationViewModel.setCurrentScreen(Screen.StartScreen)
                }
            }
            else -> {
            }
        }
    }

    when (userDataState) {
        is UserDataState.Loading, UserDataState.None -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        }
        is UserDataState.Error -> {
            StartScreen(
                onNavigateToMain = {
                    navigationViewModel.setCurrentScreen(Screen.ScreenBNav)
                },
                viewModel = profileViewModel
            )
        }
        is UserDataState.Success -> {
            val userData = (userDataState as UserDataState.Success).userData

            when (currentScreen) {
                Screen.StartScreen -> {
                    if (userData.userName.isNotBlank()) {
                        LaunchedEffect(Unit) {
                            navigationViewModel.setCurrentScreen(Screen.ScreenBNav)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    } else {
                        StartScreen(
                            onNavigateToMain = {
                                navigationViewModel.setCurrentScreen(Screen.ScreenBNav)
                            },
                            viewModel = profileViewModel
                        )
                    }
                }
                Screen.ScreenBNav -> {
                    if (userData.userName.isBlank()) {
                        LaunchedEffect(Unit) {
                            navigationViewModel.setCurrentScreen(Screen.StartScreen)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    } else {
                        ScreenWithBottomNav(
                            onNavigateToChat = { navigationViewModel.setCurrentScreen(Screen.ScreenChatGpt) },
                            onNavigateToBreathing = { navigationViewModel.setCurrentScreen(Screen.ScreenChooseDifficulty) },
                            gptViewModel = gptViewModel,
                            breathingViewModel = breathingViewModel,
                            noteViewModel = noteViewModel,
                            mainScreenViewModel = mainScreenViewModel,
                            bottomNavViewModel = bottomNavViewModel,
                            techniqueSelectionScreenViewModel = techniqueSelectionScreenViewModel,
                            profileViewModel = profileViewModel
                        )
                    }
                }
                Screen.ScreenChatGpt -> ChatWithGptScreen(
                    onNavigateBack = { navigationViewModel.setCurrentScreen(Screen.ScreenBNav) },
                    viewModel = gptViewModel
                )

                Screen.ScreenChooseDifficulty -> ChooseDifficultyScreen(
                    onNavigateBack = { navigationViewModel.setCurrentScreen(Screen.ScreenBNav) },
                    onNext = { navigationViewModel.setCurrentScreen(Screen.ScreenBreathing) },
                    viewModel = breathingViewModel
                )

                Screen.ScreenBreathing -> BreathingScreen(
                    onNavigateBack = { navigationViewModel.setCurrentScreen(Screen.ScreenBNav) },
                    viewModel = breathingViewModel
                )
            }
        }
    }
}