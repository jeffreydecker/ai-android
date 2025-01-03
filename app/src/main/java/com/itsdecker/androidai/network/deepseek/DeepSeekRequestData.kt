package com.itsdecker.androidai.network.deepseek

import com.google.gson.annotations.SerializedName
import com.itsdecker.androidai.network.Message

data class DeepSeekRequest(
    @SerializedName(value = "max_tokens")
    val maxTokens: Int = DEEP_SEEK_DEFAULT_MAX_TOKENS,
    val model: String = DEEP_SEEK_CHAT,
    val temperature: Double = DEEP_SEEK_DEFAULT_TEMPERATURE,
    val messages: List<Message>,
)

data class DeepSeekResponse(
    val id: String,
    @SerializedName("object")
    val apiObject: String, // Should always be "chat.completion"
    val choices: List<Choice>,
    val created: Long,
    val model: String,
    @SerializedName("system_fingerprint")
    val systemFingerprint: String,    val usage: Usage,
)

data class Choice(
    @SerializedName("finish_reason")
    val finishReason: FinishReason,
    val index: Int,
    val message: Message,
)

data class Usage(
    @SerializedName("completion_tokens")
    val completionTokens: Int,
    @SerializedName("prompt_tokens")
    val promptTokens: Int,
    @SerializedName("total_tokens")
    val totalTokens: Int,
)

enum class FinishReason {
    @SerializedName("content_filter")
    CONTENT_FILTER,
    @SerializedName("insufficient_system_resource")
    INSUFFICIENT_SYSTEM_RESOURCE,
    @SerializedName("length")
    LENGTH,
    @SerializedName("stop")
    STOP,
    @SerializedName("tool_calls")
    TOOL_CALLS,
}