package com.example.muzzchat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val isRecipient: Boolean,
    val timestamp: Long
)