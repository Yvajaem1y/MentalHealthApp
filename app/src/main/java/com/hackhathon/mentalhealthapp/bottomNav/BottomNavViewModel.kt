package com.hackhathon.mentalhealthapp.bottomNav

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModel @Inject constructor() : ViewModel() {

    private var _navController: NavHostController? = null
    private val _currentRoute = MutableStateFlow("home")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    fun setNavController(navController: NavHostController) {
        _navController = navController
        _isInitialized.value = true
    }

    fun navigateTo(route: String) {
        viewModelScope.launch {
            if (_isInitialized.value) {
                _navController?.navigate(route) {
                    popUpTo(0)
                    launchSingleTop = true
                    restoreState = true
                }
                _currentRoute.value = route
            }
        }
    }

    fun updateCurrentRoute(route: String) {
        viewModelScope.launch {
            _currentRoute.value = route
        }
    }
}
