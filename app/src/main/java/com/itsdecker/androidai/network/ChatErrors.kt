package com.itsdecker.androidai.network

sealed class ChatApiError : Exception() {
    data class InvalidRequest(override val message: String) : ChatApiError()
    data class AuthenticationError(override val message: String = DEFAULT_AUTH_ERROR) : ChatApiError()
    data class InsufficientBalance(override val message: String = DEFAULT_BALANCE_ERROR) : ChatApiError()
    data class PermissionError(override val message: String = DEFAULT_PERMISSIONS_ERROR) : ChatApiError()
    data class NotFoundError(override val message: String = DEFAULT_NOT_FOUND_ERROR) : ChatApiError()
    data class RequestTooLarge(override val message: String = DEFAULT_LARGE_REQUEST_ERROR) : ChatApiError()
    data class RateLimitError(override val message: String = DEFAULT_RATE_LIMIT_ERROR) : ChatApiError()
    data class ApiError(override val message: String = DEFAULT_API_ERROR) : ChatApiError()
    data class OverloadedError(override val message: String) : ChatApiError()
    data class NetworkError(override val message: String) : ChatApiError()
    data class UnknownError(override val message: String) : ChatApiError()

    companion object {
        private const val DEFAULT_INVALID_REQUEST_ERROR = "Invalid request"
        private const val DEFAULT_INVALID_REQUEST_PARAMS_ERROR = "Invalid request parameters"
        private const val DEFAULT_AUTH_ERROR = "Invalid API key or authentication failed"
        private const val DEFAULT_BALANCE_ERROR = "Insufficient credit balance for this request"
        private const val DEFAULT_PERMISSIONS_ERROR = "Insufficient permissions for this resource"
        private const val DEFAULT_NOT_FOUND_ERROR = "Requested resource not found"
        private const val DEFAULT_LARGE_REQUEST_ERROR = "Request exceeds maximum allowed size"
        private const val DEFAULT_RATE_LIMIT_ERROR = "Rate limit exceeded. Please try again later"
        private const val DEFAULT_API_ERROR = "Internal API error"
        private const val DEFAULT_OVERLOADED_ERROR = "API is temporarily overloaded. Please try again later"
        private const val DEFAULT_UNKNOWN_ERROR = "Unknown error occurred"

        fun errorForCode(errorCode: Int, errorResponse: ErrorResponse?): ChatApiError =
            when (errorCode) {
                400 -> InvalidRequest(errorResponse?.error?.message ?: DEFAULT_INVALID_REQUEST_ERROR)
                401 -> AuthenticationError(errorResponse?.error?.message ?: DEFAULT_AUTH_ERROR)
                402 -> InsufficientBalance(errorResponse?.error?.message ?: DEFAULT_BALANCE_ERROR)
                403 -> PermissionError(errorResponse?.error?.message ?: DEFAULT_PERMISSIONS_ERROR)
                404 -> NotFoundError(errorResponse?.error?.message ?: DEFAULT_NOT_FOUND_ERROR)
                413 -> RequestTooLarge(errorResponse?.error?.message ?: DEFAULT_LARGE_REQUEST_ERROR)
                422 -> InvalidRequest(errorResponse?.error?.message ?: DEFAULT_INVALID_REQUEST_PARAMS_ERROR)
                429 -> RateLimitError(errorResponse?.error?.message ?: DEFAULT_RATE_LIMIT_ERROR)
                500 -> ApiError(errorResponse?.error?.message ?: DEFAULT_API_ERROR)
                503,
                529 -> OverloadedError(errorResponse?.error?.message ?: DEFAULT_OVERLOADED_ERROR)
                else -> UnknownError(errorResponse?.error?.message ?: DEFAULT_UNKNOWN_ERROR)
            }
    }
}