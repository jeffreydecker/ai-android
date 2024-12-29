package com.itsdecker.androidai.screens.preview

import com.itsdecker.androidai.database.ConversationEntity
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_ASSISTANT
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_USER
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
                role = ANTHROPIC_MESSENGER_ROLE_USER,
                content = "what did I say?",
            ),
            MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                role = ANTHROPIC_MESSENGER_ROLE_ASSISTANT,
                content = "this is the start of our conversation... you said nothing",
            ),
        )
    )
}