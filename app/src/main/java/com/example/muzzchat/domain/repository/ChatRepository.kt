package com.example.muzzchat.domain.repository

import com.example.muzzchat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(): Flow<List<ChatMessage>>
    suspend fun saveMessage(message: ChatMessage)
}