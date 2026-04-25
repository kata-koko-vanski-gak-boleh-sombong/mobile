package com.project.edu_law.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.data.repository.ScenarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScenarioViewModel(private val repository: ScenarioRepository) : ViewModel() {

    companion object {
        fun provideFactory(repository: ScenarioRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ScenarioViewModel(repository) as T
            }
        }
    }

    // 1. Mengambil semua data untuk halaman daftar (LegalScenarioScreen)
    // stateIn digunakan agar Flow dari Room tetap aktif selama ViewModel hidup
    val allScenarios: StateFlow<List<ScenarioEntity>> = repository.getAllScenarios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. State untuk satu skenario terpilih (ScenarioOverviewScreen)
    private val _selectedScenario = MutableStateFlow<ScenarioEntity?>(null)
    val selectedScenario = _selectedScenario.asStateFlow()

    fun getScenarioById(id: String) {
        viewModelScope.launch {
            _selectedScenario.value = repository.getScenarioById(id)
        }
    }
}