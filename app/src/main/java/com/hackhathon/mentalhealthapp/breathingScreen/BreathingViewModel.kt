package com.hackhathon.mentalhealthapp.breathingScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class BreathingViewModel @Inject constructor() : ViewModel() {

    private val _selectedTechnique = MutableStateFlow<BreathingTechnique?>(null)
    val selectedTechnique: StateFlow<BreathingTechnique?> = _selectedTechnique.asStateFlow()

    // Breathing state
    private val _isBreathingActive = MutableStateFlow(false)
    val isBreathingActive: StateFlow<Boolean> = _isBreathingActive.asStateFlow()

    // Current phase
    private val _currentPhase = MutableStateFlow(BreathingPhase.IDLE)
    val currentPhase: StateFlow<BreathingPhase> = _currentPhase.asStateFlow()

    // Time remaining in current phase
    private val _timeRemaining = MutableStateFlow(0)
    val timeRemaining: StateFlow<Int> = _timeRemaining.asStateFlow()

    // Session progress
    private val _completedCycles = MutableStateFlow(0)
    val completedCycles: StateFlow<Int> = _completedCycles.asStateFlow()

    private val _totalCycles = MutableStateFlow(0)
    val totalCycles: StateFlow<Int> = _totalCycles.asStateFlow()

    // Progress bar data
    private val _sessionProgress = MutableStateFlow(0f)
    val sessionProgress: StateFlow<Float> = _sessionProgress.asStateFlow()

    private val _totalSessionTime = MutableStateFlow(0)
    val totalSessionTime: StateFlow<Int> = _totalSessionTime.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0)
    val elapsedTime: StateFlow<Int> = _elapsedTime.asStateFlow()

    // Timers
    private var phaseTimer: Timer? = null
    private var sessionTimer: Timer? = null

    // Technique selection
    fun selectTechnique(technique: BreathingTechnique) {
        viewModelScope.launch {
            _selectedTechnique.value = technique
            _totalCycles.value = technique.recommendedCycles
            calculateTotalSessionTime()
            resetBreathing()
        }
    }

    fun startBreathing() {
        _isBreathingActive.value = true
        _completedCycles.value = 0
        _elapsedTime.value = 0
        _sessionProgress.value = 0f

        startSessionTimer()

        startNextPhase()
    }

    fun stopBreathing() {
        _isBreathingActive.value = false
        phaseTimer?.cancel()
        sessionTimer?.cancel()
        _currentPhase.value = BreathingPhase.IDLE
    }

    fun selectDifficulty(count : Int){
        if (_selectedTechnique.value !=null){
            _selectedTechnique.value?.recommendedCycles=count
        }
    }

    fun pauseBreathing() {
        _isBreathingActive.value = false
        phaseTimer?.cancel()
        sessionTimer?.cancel()
    }

    private fun calculateTotalSessionTime() {
        val technique = _selectedTechnique.value ?: return
        val cycleTime = technique.pattern.inhale + technique.pattern.hold +
                technique.pattern.exhale + technique.pattern.holdAfterExhale
        _totalSessionTime.value = cycleTime * _totalCycles.value
    }

    private fun startSessionTimer() {
        sessionTimer = Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    _elapsedTime.value++
                    if (_totalSessionTime.value > 0) {
                        _sessionProgress.value = (_elapsedTime.value.toFloat() / _totalSessionTime.value.toFloat())
                            .coerceIn(0f, 1f)
                    }

                    // Auto-complete when time is up
                    if (_elapsedTime.value >= _totalSessionTime.value) {
                        stopBreathing()
                        _sessionProgress.value = 1f
                    }
                }
            }, 1000, 1000)
        }
    }

    // Move to next breathing phase
    private fun startNextPhase() {
        val technique = _selectedTechnique.value ?: return

        val nextPhase = when (_currentPhase.value) {
            BreathingPhase.IDLE -> BreathingPhase.INHALE
            BreathingPhase.INHALE -> BreathingPhase.HOLD
            BreathingPhase.HOLD -> BreathingPhase.EXHALE
            BreathingPhase.EXHALE -> {
                if (technique.pattern.holdAfterExhale > 0) {
                    BreathingPhase.HOLD_AFTER_EXHALE
                } else {
                    completeCycle()
                    BreathingPhase.INHALE
                }
            }
            BreathingPhase.HOLD_AFTER_EXHALE -> {
                completeCycle()
                BreathingPhase.INHALE
            }
        }

        _currentPhase.value = nextPhase

        val duration = when (nextPhase) {
            BreathingPhase.INHALE -> technique.pattern.inhale
            BreathingPhase.HOLD -> technique.pattern.hold
            BreathingPhase.EXHALE -> technique.pattern.exhale
            BreathingPhase.HOLD_AFTER_EXHALE -> technique.pattern.holdAfterExhale
            else -> 0
        }

        _timeRemaining.value = duration

        // Start phase timer
        phaseTimer?.cancel()
        if (duration > 0 && _isBreathingActive.value) {
            phaseTimer = Timer().apply {
                scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        if (_timeRemaining.value > 0) {
                            _timeRemaining.value--
                        } else {
                            startNextPhase()
                        }
                    }
                }, 1000, 1000)
            }
        }
    }

    // Complete one breathing cycle
    private fun completeCycle() {
        _completedCycles.value++

        // Check if all cycles are completed
        if (_completedCycles.value >= _totalCycles.value) {
            stopBreathing()
            _sessionProgress.value = 1f
        }
    }

    // Reset breathing session
    private fun resetBreathing() {
        stopBreathing()
        _timeRemaining.value = 0
        _completedCycles.value = 0
        _elapsedTime.value = 0
        _sessionProgress.value = 0f
        calculateTotalSessionTime()
    }

    // Format time for display (MM:SS)
    fun getFormattedTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%d:%02d", minutes, remainingSeconds)
    }

    // Clean up timers when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        phaseTimer?.cancel()
        sessionTimer?.cancel()
    }
}

enum class BreathingPhase {
    IDLE, INHALE, HOLD, EXHALE, HOLD_AFTER_EXHALE
}