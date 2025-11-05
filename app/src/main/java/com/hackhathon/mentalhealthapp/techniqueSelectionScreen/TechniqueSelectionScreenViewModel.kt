package com.hackhathon.mentalhealthapp.techniqueSelectionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackhathon.data.BreathingTechniqueRepository
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingTechnique
import com.hackhathon.mentalhealthapp.breathingScreen.BreathingTechniques
import com.hackhathon.mentalhealthapp.techniqueSelectionScreen.utils.toData
import com.hackhathon.mentalhealthapp.techniqueSelectionScreen.utils.toUIData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.security.Provider
import javax.inject.Inject

@HiltViewModel
class TechniqueSelectionScreenViewModel @Inject constructor(
    val breathingTechniqueRepository: BreathingTechniqueRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(0)
    val selectedFilter: StateFlow<Int> = _selectedFilter.asStateFlow()

    private val _items = MutableStateFlow(BreathingTechniques.techniques)

    val filteredItems: StateFlow<List<BreathingTechnique>> =
        _selectedFilter.flatMapLatest { filter ->
            when (filter) {
                0 -> _items
                1 -> breathingTechniqueRepository.observeFavoriteTechnique()
                    .map { favorites ->
                        favorites.map { it.toUIData()}
                    }
                else -> _items
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _items.value
        )

    fun updateFilter(newFilter: Int) {
        _selectedFilter.value = newFilter
    }

    fun isFavoriteTechnique(breathingTechnique: BreathingTechnique) {
        viewModelScope.launch {
            breathingTechniqueRepository.setIsFavoriteTechnique(breathingTechnique.toData())
        }
    }

    fun isUnfavoriteTechnique(breathingTechnique: BreathingTechnique) {
        viewModelScope.launch {
            breathingTechniqueRepository.deleteIsFavoriteTechnique(breathingTechnique.toData())
        }
    }

    val favoriteTechniques: StateFlow<List<BreathingTechnique>> =
        breathingTechniqueRepository.observeFavoriteTechnique()
            .map { favorites -> favorites.map { it.toUIData() } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

}