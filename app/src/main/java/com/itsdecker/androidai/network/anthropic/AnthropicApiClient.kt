package com.itsdecker.androidai.network.anthropic

import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.ChatApiClient
import com.itsdecker.androidai.network.ChatRole
import com.itsdecker.androidai.network.ChatRole.Companion.toRole
import com.itsdecker.androidai.network.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnthropicApiClient @Inject constructor() : ChatApiClient(baseUrl = ANTHROPIC_BASE_URL) {

    private val api = retrofit.create(AnthropicApi::class.java)

    override suspend fun internalSendMessage(
        apiKey: String,
        conversationId: String,
        conversationHistory: List<MessageEntity>
    ): MessageEntity? {
        val request = AnthropicRequest(
            messages = conversationHistory.map {
                Message(
                    role = it.role.toAnthropicRole(),
                    content = it.content,
                )
            }
        )

        val response = api.sendMessage(apiKey, request = request)

        return response.content.firstOrNull()?.text?.let {
            MessageEntity(
                role = ChatRole.Assistant.value,
                content = it,
                conversationId = conversationId,
            )
        }
    }

    private fun String.toAnthropicRole() = when (this.toRole()) {
        ChatRole.Assistant -> ANTHROPIC_ROLE_ASSISTANT
        ChatRole.User -> ANTHROPIC_ROLE_USER
        ChatRole.System -> ANTHROPIC_ROLE_USER // TODO - This should actually end up being a system prompt
    }
}