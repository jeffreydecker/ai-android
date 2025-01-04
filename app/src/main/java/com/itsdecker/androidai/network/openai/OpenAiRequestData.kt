package com.itsdecker.androidai.network.openai

import com.google.gson.annotations.SerializedName
import com.itsdecker.androidai.network.Message


data class OpenAiRequest(
    @SerializedName(value = "max_completion_tokens")
    val maxTokens: Int = OPEN_AI_DEFAULT_MAX_TOKENS,
    val model: String = OPEN_AI_MODEL_DEFAULT,
    val messages: List<Message>,
    val temperature: Double = OPEN_AI_DEFAULT_TEMPERATURE,
)

data class OpenAiResponse(
    val id: String,
    @SerializedName("object")
    val apiObject: String, // Should always be "chat.completion"
    val choices: List<Choice>,
    val created: Long,
    val model: String,
    @SerializedName("system_fingerprint")
    val systemFingerprint: String,
    val usage: Usage,
)

data class Choice(
    @SerializedName("finish_reason")
    val finishReason: FinishReason,
    val index: Int,
    val message: Message,
    val refusal: String,
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
    @SerializedName("function_call")
    FUNCTION_CALL,
    @SerializedName("length")
    LENGTH,
    @SerializedName("stop")
    STOP,
    @SerializedName("tool_calls")
    TOOL_CALLS,
}