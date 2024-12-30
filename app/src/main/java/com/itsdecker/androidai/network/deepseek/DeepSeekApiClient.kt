package com.itsdecker.androidai.network.deepseek

import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.ChatApiClient
import com.itsdecker.androidai.network.ChatRole
import com.itsdecker.androidai.network.ChatRole.Companion.toRole
import com.itsdecker.androidai.network.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepSeekApiClient @Inject constructor() : ChatApiClient(baseUrl = DEEP_SEEK_BASE_URL) {

    private val api = retrofit.create(DeepSeekApi::class.java)

    override suspend fun internalSendMessage(
        apiKey: String,
        conversationId: String,
        conversationHistory: List<MessageEntity>,
    ): MessageEntity? {
        val request = DeepSeekRequest(
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
        ChatRole.Assistant -> DEEP_SEEK_ROLE_ASSISTANT
        ChatRole.User -> DEEP_SEEK_ROLE_USER
    }
}