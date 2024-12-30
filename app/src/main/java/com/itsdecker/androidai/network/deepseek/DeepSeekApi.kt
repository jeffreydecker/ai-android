package com.itsdecker.androidai.network.deepseek

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepSeekApi {
    @POST("chat/completions")
    suspend fun sendMessage(
        @Header("Authorization") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: DeepSeekRequest
    ): DeepSeekResponse
}