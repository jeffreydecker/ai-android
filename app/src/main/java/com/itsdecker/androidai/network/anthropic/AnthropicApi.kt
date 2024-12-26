package com.itsdecker.androidai.network.anthropic

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Retrofit Api Interface for Anthropic
interface AnthropicApi {
    @POST("messages")
    suspend fun sendMessage(
        @Header("x-api-key") apiKey: String,
        @Header("anthropic-version") version: String = ANTHROPIC_VERSION,
        @Body request: AnthropicRequest
    ): AnthropicResponse
}