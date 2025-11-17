package com.example.muzzchat.data.repository

import com.example.muzzchat.data.local.ChatMessageDao
import com.example.muzzchat.data.local.ChatMessageEntity
import com.example.muzzchat.domain.model.ChatMessage
import com.example.muzzchat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class ChatRepositoryImpl(
    private val dao: ChatMessageDao
): ChatRepository {
    override fun getMessages(): Flow<List<ChatMessage>> =
        dao.getAllMessages().map {
            it.map { it.toDomain() }
        }

    override suspend fun saveMessage(message: ChatMessage) {
        dao.insertMessage(message.toEntity())
    }

}

fun ChatMessageEntity.toDomain() = ChatMessage(
    message = message,
    isRecipient = isRecipient,
    timestamp = Instant.ofEpochMilli(timestamp),
    isSectionRequired = false,
    isClusterMessage = false
)

fun ChatMessage.toEntity() = ChatMessageEntity(
    message = message,
    isRecipient = isRecipient,
    timestamp = timestamp.toEpochMilli()
)