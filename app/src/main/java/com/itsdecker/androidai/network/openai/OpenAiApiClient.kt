package com.itsdecker.androidai.network.openai

import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.ChatApiClient
import com.itsdecker.androidai.network.ChatRole
import com.itsdecker.androidai.network.ChatRole.Companion.toRole
import com.itsdecker.androidai.network.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenAiApiClient @Inject constructor() : ChatApiClient(baseUrl = OPEN_AI_BASE_URL) {

    private val api = retrofit.create(OpenAiApi::class.java)

    override suspend fun internalSendMessage(
        apiKey: String,
        conversationId: String,
        conversationHistory: List<MessageEntity>,
    ): MessageEntity? {
        val request = OpenAiRequest(
            messages = conversationHistory.map {
                Message(
                    role = it.role.toDeepSeekRole(),
                    content = it.content,
                )
            }
        )

        val response = api.sendMessage(
            apiKey = "Bearer $apiKey",
            request = request,
        )

        return response.choices.firstOrNull()?.message?.content?.let {
            MessageEntity(
                role = ChatRole.Assistant.value,
                content = it,
                conversationId = conversationId,
            )
        }
    }

    private fun String.toDeepSeekRole() = when (this.toRole()) {
        ChatRole.Assistant -> OPEN_AI_ROLE_ASSISTANT
        ChatRole.User -> OPEN_AI_ROLE_USER
        ChatRole.System -> OPEN_AI_ROLE_SYSTEM

    }
}