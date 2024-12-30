package com.itsdecker.androidai.network.deepseek

import com.google.gson.annotations.SerializedName
import com.itsdecker.androidai.network.Message

data class DeepSeekRequest(
    val model: String = DEEP_SEEK_CHAT,
    @SerializedName(value = "max_tokens")
    val maxTokens: Int = DEEP_SEEK_DEFAULT_MAX_TOKENS,
    val temperature: Double = DEEP_SEEK_DEFAULT_TEMPERATURE,
    val stream: Boolean = false,
    val messages: List<Message>,
)

data class DeepSeekResponse(
    val id: String,
    @SerializedName("object")
    val apiObject: String, // Should always be "chat.completion"
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage,
    @SerializedName("system_fingerprint")
    val systemFingerprint: String,
)

data class Choice(
    val index: Int,
    val message: Message,
    @SerializedName("finish_reason")
    val finishReason: String,
)

data class Usage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int,
    @SerializedName("completion_tokens")
    val completionTokens: Int,
    @SerializedName("total_tokens")
    val totalTokens: Int,
    @SerializedName("prompt_cache_hit_tokens")
    val promptCacheHitTokens: Int,
    @SerializedName("prompt_cache_miss_tokens")
    val promptCacheMissTokens: Int,
)

// TODO - Map to this enum
enum class FinishReason {
    STOP, LENGTH, CONTENT_FILTER, TOOL_CALLS, INSUFFICIENT_SYSTEM_RESOURCE
}