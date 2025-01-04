package com.itsdecker.androidai.network

data class Message(
    val role: String,
    val content: String
)

data class ErrorResponse(
    val error: ErrorDetail
)

data class ErrorDetail(
    val type: String,
    val message: String
)

sealed class ChatRole(val value: String) {
    data object User : ChatRole(USER_ROLE)
    data object Assistant : ChatRole(ASSISTANT_ROLE)
    data object System : ChatRole(SYSTEM_ROLE)

    companion object {
        const val USER_ROLE = "user"
        const val ASSISTANT_ROLE = "assistant"
        const val SYSTEM_ROLE = "system"

        fun String.toRole(): ChatRole = when(this) {
            USER_ROLE -> User
            ASSISTANT_ROLE -> Assistant
            SYSTEM_ROLE -> System
            else -> throw IllegalArgumentException("Invalid role: $this")
        }
    }
}