package com.itsdecker.androidai.screens.preview

import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationEntity
import com.itsdecker.androidai.database.ConversationWithApiKey

fun conversationsList() = listOf(
    ConversationWithApiKey(
        conversation = ConversationEntity(
            apiKeyId = "fakeApiKey",
            title = "Conversation 1",
        ),
        apiKey = ApiKeyEntity(
            name = "Anthropic Key",
            description = "My Anthropic API key",
            apiKey = "fakeKey",
            chatModel = SupportedProvider.Anthropic,
        )
    ),
    ConversationWithApiKey(
        conversation = ConversationEntity(
            apiKeyId = "fakeApiKey",
            title = "Conversation 2",
        ),
        apiKey = ApiKeyEntity(
            name = "OpenAI Key",
            description = "My OpenAI API key",
            apiKey = "fakeKey",
            chatModel = SupportedProvider.OpenAI,
        )
    ),
    ConversationWithApiKey(
        conversation = ConversationEntity(
            apiKeyId = "fakeApiKey",
            title = "Conversation 3",
        ),
        apiKey = ApiKeyEntity(
            name = "Google Key",
            description = "My Google API key",
            apiKey = "fakeKey",
            chatModel = SupportedProvider.Google,
        )
    ),
)