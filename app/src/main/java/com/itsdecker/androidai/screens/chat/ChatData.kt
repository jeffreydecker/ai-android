package com.itsdecker.androidai.screens.chat

import java.util.UUID

data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class Conversation(
    val messages: List<ChatMessage>,
    val id: String = UUID.randomUUID().toString()
)

// TODO - Add a role enum once we integrate with other LLM providers