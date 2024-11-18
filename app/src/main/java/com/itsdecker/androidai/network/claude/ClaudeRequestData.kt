package com.itsdecker.androidai.network.claude

import com.google.gson.annotations.SerializedName

// Claude related request and response classes including

data class Message(
    val role: String,
    val content: String
)

data class ClaudeRequest(
    val model: String = ANTHROPIC_CLAUDE_3_5_MODEL_SONNET, // TODO - Make this selectable
    @SerializedName(value = "max_tokens")
    val maxTokens: Int = ANTHROPIC_API_DEFAULT_MAX_TOKENS,
    val messages: List<Message>
)

data class ContentItem(
    val text: String,
    val type: String = ANTHROPIC_API_DEFAULT_CONTENT_TYPE
)

data class ClaudeResponse(
    val content: List<ContentItem>,
    val role: String,
    val id: String
)

data class ErrorResponse(
    val error: ErrorDetail
)

data class ErrorDetail(
    val type: String,
    val message: String
)

sealed class ClaudeApiError : Exception() {
    data class InvalidRequest(override val message: String) : ClaudeApiError()
    data class AuthenticationError(override val message: String = "Invalid API key or authentication failed") :
        ClaudeApiError()

    data class PermissionError(override val message: String = "Insufficient permissions for this resource") :
        ClaudeApiError()

    data class NotFoundError(override val message: String = "Requested resource not found") :
        ClaudeApiError()

    data class RequestTooLarge(override val message: String = "Request exceeds maximum allowed size") :
        ClaudeApiError()

    data class RateLimitError(override val message: String = "Rate limit exceeded. Please try again later") :
        ClaudeApiError()

    data class ApiError(override val message: String = "Anthropic API internal error") :
        ClaudeApiError()

    data class OverloadedError(override val message: String = "API is temporarily overloaded. Please try again later") :
        ClaudeApiError()

    data class NetworkError(override val message: String) : ClaudeApiError()
    data class UnknownError(override val message: String) : ClaudeApiError()

    companion object {
        fun errorForCode(errorCode: Int, errorResponse: ErrorResponse?): ClaudeApiError =
            when (errorCode) {
                400 -> InvalidRequest(
                    errorResponse?.error?.message ?: "Invalid request format"
                )

                401 -> AuthenticationError(
                    errorResponse?.error?.message ?: "Authentication failed"
                )

                403 -> PermissionError(
                    errorResponse?.error?.message ?: "Permission denied"
                )

                404 -> NotFoundError(
                    errorResponse?.error?.message ?: "Resource not found"
                )

                413 -> RequestTooLarge(
                    errorResponse?.error?.message ?: "Request too large"
                )

                429 -> RateLimitError(
                    errorResponse?.error?.message ?: "Rate limit exceeded"
                )

                500 -> ApiError(
                    errorResponse?.error?.message ?: "Internal server error"
                )

                529 -> OverloadedError(
                    errorResponse?.error?.message ?: "Service overloaded"
                )

                else -> UnknownError(
                    errorResponse?.error?.message ?: "Unknown error occurred"
                )
            }
    }

}