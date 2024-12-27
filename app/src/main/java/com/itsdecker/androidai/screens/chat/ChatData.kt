package com.itsdecker.androidai.screens.chat

import java.util.UUID

data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val messages: List<ChatMessage>,
    val lastUpdated: Long = System.currentTimeMillis(),
)