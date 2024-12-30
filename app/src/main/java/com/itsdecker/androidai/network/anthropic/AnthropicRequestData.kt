package com.itsdecker.androidai.network.anthropic

import com.google.gson.annotations.SerializedName
import com.itsdecker.androidai.network.Message

// TODO - Fill in more fields
data class AnthropicRequest(
    val model: String = ANTHROPIC_CLAUDE_MODEL_DEFAULT, // TODO - Make this selectable
    @SerializedName(value = "max_tokens")
    val maxTokens: Int = ANTHROPIC_API_DEFAULT_MAX_TOKENS,
    val temperature: Double = ANTHROPIC_API_DEFAULT_TEMPERATURE,
    val system: String? = null,
    val messages: List<Message>
)

data class AnthropicResponse(
    val id: String,
    val role: String,
    val content: List<ContentItem>,
)

data class ContentItem(
    val text: String,
    val type: String,
)