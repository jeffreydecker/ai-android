package com.itsdecker.androidai.network.anthropic

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AnthropicApi {
    @POST("messages")
    suspend fun sendMessage(
        @Header("x-api-key") apiKey: String,
        @Header("content-type") contentType: String = "application/json",
        @Header("anthropic-version") version: String = ANTHROPIC_VERSION,
        @Body request: AnthropicRequest
    ): AnthropicResponse
}