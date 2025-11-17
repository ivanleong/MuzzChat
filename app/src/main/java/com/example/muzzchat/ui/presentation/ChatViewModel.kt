package com.example.muzzchat.ui.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzzchat.domain.model.ChatMessage
import com.example.muzzchat.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMessages()
    }

    fun loadMessages() {
        viewModelScope.launch {
            repository.getMessages()
                .catch { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
                .collect { messages ->
                    processSectionsAndUpdate(messages)
                }
        }
    }

    fun saveMessage(text: String, isRecipient: Boolean) {
        viewModelScope.launch {
            repository.saveMessage(ChatMessage(text, isRecipient, Instant.now(), false, false))
        }
    }

    internal fun processSectionsAndUpdate(messageList: List<ChatMessage>){
        val processed = processSections(messageList)
        _uiState.update { it.copy(messageList = processed)}
    }

    @VisibleForTesting
    internal fun processSections(messages: List<ChatMessage>): List<ChatMessage> {
        var previousTimestamp: Instant? = null
        var previousUser: Boolean? = null

        return messages.mapIndexed { index, message ->
            val timeDiff = previousTimestamp?.let { Duration.between(it, message.timestamp) }

            val isSectionRequired =
                if (index == 0) true
                else (timeDiff?.toMinutes()?.let{ it >= 60} ?: true)

            val isClusterMessage =
                if (index == 0) false
                else (timeDiff?.seconds?.let{ it < 20 } ?: false && previousUser == message.isRecipient)

            previousTimestamp = message.timestamp
            previousUser = message.isRecipient

            message.copy(
                isSectionRequired = isSectionRequired,
                isClusterMessage = isClusterMessage)
        }
    }
}

data class ChatUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val messageList: List<ChatMessage> = emptyList()
)