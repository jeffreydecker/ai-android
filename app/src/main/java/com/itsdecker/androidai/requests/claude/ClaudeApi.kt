package com.itsdecker.androidai.requests.claude

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Retrofit Api Interface for Anthropic Claude
interface ClaudeApi {
    @POST("messages")
    suspend fun sendMessage(
        @Header("x-api-key") apiKey: String,
        @Header("anthropic-version") version: String = ANTHROPIC_VERSION,
        @Body request: ClaudeRequest
    ): ClaudeResponse
}