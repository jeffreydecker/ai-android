package com.itsdecker.androidai.data.respository

import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.ChatApiClient
import com.itsdecker.androidai.network.anthropic.AnthropicApiClient
import com.itsdecker.androidai.network.deepseek.DeepSeekApiClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiRepository @Inject constructor(
    private val anthropicApi: AnthropicApiClient,
    private val deepSeekApi: DeepSeekApiClient,
) {

    suspend fun sendMessage(
        apiKey: ApiKeyEntity,
        conversationId: String,
        conversationHistory: List<MessageEntity>,
    ): MessageEntity = apiKey.getApiClient().sendMessage(
        apiKey = apiKey.apiKey,
        conversationId = conversationId,
        conversationHistory = conversationHistory,
    )

    private fun ApiKeyEntity.getApiClient(): ChatApiClient =
        when(chatModel) {
            SupportedProvider.Anthropic -> anthropicApi
            SupportedProvider.DeepSeek -> deepSeekApi
            SupportedProvider.OpenAI,
            SupportedProvider.Google,
            SupportedProvider.OpenRouter,
            SupportedProvider.UNINITIALIZED,
            SupportedProvider.UNKNOWN -> throw IllegalStateException("Unimplemented Api Key Type")
        }
}