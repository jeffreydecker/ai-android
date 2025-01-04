package com.itsdecker.androidai.network.anthropic

import com.google.gson.annotations.SerializedName
import com.itsdecker.androidai.network.Message

data class AnthropicRequest(
    @SerializedName(value = "max_tokens")
    val maxTokens: Int = ANTHROPIC_API_DEFAULT_MAX_TOKENS,
    val messages: List<Message>,
    val model: String = ANTHROPIC_CLAUDE_MODEL_DEFAULT, // TODO - Make this selectable
    val system: String? = null,
    val temperature: Double = ANTHROPIC_API_DEFAULT_TEMPERATURE,
)

data class AnthropicResponse(
    val id: String,
    val content: List<ContentItem>,
    val model: String,
    val role: String,
    @SerializedName("stop_reason")
    val stopReason: StopReason,
    val usage: Usage,
)

data class ContentItem(
    val text: String,
    val type: String,
)

data class Usage(
    @SerializedName("input_tokens")
    val inputTokens: Int,
    @SerializedName("output_tokens")
    val outputTokens: Int,
)

enum class StopReason {
    @SerializedName("end_turn")
    END_TURN,

    @SerializedName("max_tokens")
    MAX_TOKENS,

    @SerializedName("stop_sequence")
    STOP_SEQUENCE,

    @SerializedName("tool_use")
    TOOL_USE,
}