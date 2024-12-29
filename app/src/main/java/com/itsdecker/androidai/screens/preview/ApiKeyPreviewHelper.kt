package com.itsdecker.androidai.screens.preview

import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ApiKeyEntity

fun apiKeyPreviewList(): List<ApiKeyEntity> = listOf(
    ApiKeyEntity(
        id = "1",
        createdAt = System.currentTimeMillis(),
        name = "My Anthropic Key",
        description = "This is my general purpose Anthropic key",
        apiKey = "",
        chatModel = SupportedProvider.Anthropic,
    ),
    ApiKeyEntity(
        id = "2",
        createdAt = System.currentTimeMillis(),
        name = "My OpenAI Key",
        description = "This is my general purpose OpenAI key",
        apiKey = "",
        chatModel = SupportedProvider.OpenAI,
    ),
    ApiKeyEntity(
        id = "3",
        createdAt = System.currentTimeMillis(),
        name = "My Google Key",
        description = "This is my general purpose Google key",
        apiKey = "",
        chatModel = SupportedProvider.Google,
    )

)