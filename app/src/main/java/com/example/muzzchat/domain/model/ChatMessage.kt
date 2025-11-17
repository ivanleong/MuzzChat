package com.example.muzzchat.domain.model

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Instant.formatDayOfWeek(): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

fun Instant.formatTime(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}

data class ChatMessage(
    val message: String,
    val isRecipient: Boolean,
    var timestamp: Instant,
    var isSectionRequired: Boolean,
    var isClusterMessage: Boolean
)