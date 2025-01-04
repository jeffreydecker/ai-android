package com.itsdecker.androidai.network.openai

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApi {
    @POST("chat/completions")
    suspend fun sendMessage(
        @Header("Authorization")apiKey: String,
        @Header("Content-Type")contentType: String = "application/json",
        @Body request: OpenAiRequest
    ): OpenAiResponse
}
