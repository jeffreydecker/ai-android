package com.itsdecker.androidai.screens.preview

import com.itsdecker.androidai.database.ConversationEntity
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.ChatRole
import java.util.UUID

fun chatMessagesPreviewList(): ConversationWithMessages {
    val conversationId = UUID.randomUUID().toString()
    return ConversationWithMessages(
        conversation = ConversationEntity(
            id = conversationId,
            title = "Test Conversation",
            apiKeyId = UUID.randomUUID().toString(),
        ),
        messages = listOf(
            MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                role = ChatRole.User.value,
                content = "Hello!",
            ),
            MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                role = ChatRole.Assistant.value,
                content = "Hello! How can I help you today?",
            ),
            MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                role = ChatRole.User.value,
                content = "Tell me a joke",
            ),
        )
    )
}