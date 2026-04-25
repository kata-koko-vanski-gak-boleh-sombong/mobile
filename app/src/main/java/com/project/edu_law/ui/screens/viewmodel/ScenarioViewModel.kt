package com.project.edu_law.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.edu_law.data.entity.HistoryEntity
import com.project.edu_law.data.entity.ScenarioEntity
import com.project.edu_law.data.repository.ScenarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

class ScenarioViewModel(private val repository: ScenarioRepository) : ViewModel() {

    companion object {
        fun provideFactory(repository: ScenarioRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ScenarioViewModel(repository) as T
            }
        }
    }

    init {
        checkAndSyncData()
    }

    private fun checkAndSyncData() {
        viewModelScope.launch {
            repository.syncScenariosFromApi()
        }
    }
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating = _isGenerating.asStateFlow()

    private val _generateError = MutableStateFlow<String?>(null)
    val generateError = _generateError.asStateFlow()

    fun generateScenario(difficulty: String, character: String, onSuccess: (String) -> Unit) {
        _isGenerating.value = true
        _generateError.value = null

        val userId = "913c93b0a8cf4a2aae61731f7d7ac9f4"

        repository.generateNewSimulationAsync(userId, difficulty, character) { result ->

            viewModelScope.launch {
                if (result.isSuccess) {
                    val newId = result.getOrNull()
                    if (newId != null) {
                        onSuccess(newId)
                    }
                } else {
                    _generateError.value = result.exceptionOrNull()?.message ?: "Terjadi kesalahan"
                }

                _isGenerating.value = false
            }
        }
    }

    val allScenarios: StateFlow<List<ScenarioEntity>> = repository.getAllScenarios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedScenario = MutableStateFlow<ScenarioEntity?>(null)
    val selectedScenario = _selectedScenario.asStateFlow()

    fun getScenarioById(id: String) {
        viewModelScope.launch {
            _selectedScenario.value = repository.getScenarioById(id)
        }
    }

    fun saveQuizHistory(history: HistoryEntity) {
        viewModelScope.launch {
            repository.saveHistory(history)
        }
    }

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading = _isChatLoading.asStateFlow()

    fun clearChat() {
        _chatMessages.value = emptyList()
    }

    fun sendChatMessage(contextText: String, userQuestion: String) {
        if (userQuestion.isBlank()) return

        val userMsg = ChatMessage(userQuestion, isUser = true)
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        val formattedPrompt = """
            History/Konteks:
            $contextText
            
            Pertanyaan User:
            $userQuestion
        """.trimIndent()

        val userId = "913c93b0a8cf4a2aae61731f7d7ac9f4"

        viewModelScope.launch {
            val result = repository.askAiQuestion(userId, formattedPrompt)
            _isChatLoading.value = false

            if (result.isSuccess) {
                val aiMsg = ChatMessage(result.getOrNull() ?: "", isUser = false)
                _chatMessages.value = _chatMessages.value + aiMsg
            } else {
                val errorMsg = ChatMessage("Maaf, jaringan terputus atau server sibuk.", isUser = false)
                _chatMessages.value = _chatMessages.value + errorMsg
            }
        }
    }
}