package com.itsdecker.androidai.network.anthropic

import com.google.gson.Gson
import com.itsdecker.androidai.BuildConfig
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.screens.chat.ChatMessage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class AnthropicApiClient() {
    private val retrofit = Retrofit.Builder()
        .baseUrl(ANTHROPIC_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build())
        .build()

    private val api = retrofit.create(AnthropicApi::class.java)

    suspend fun sendMessage(
        apiKey: String,
        conversationHistory: List<MessageEntity>,
    ): String {
        val request = AnthropicRequest(
            messages = conversationHistory.map {
                Message(it.role, it.content)
            }
        )

        return try {
            val response = api.sendMessage(apiKey, request = request)
            response.content.firstOrNull()?.text ?: throw AnthropicApiError.UnknownError("Empty response")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)
            } catch (e: Exception) {
                null
            }
            throw AnthropicApiError.errorForCode(e.code(), errorResponse)
        } catch (e: IOException) {
            throw AnthropicApiError.NetworkError("Network error: ${e.message}")
        } catch (e: Exception) {
            throw AnthropicApiError.UnknownError(e.message ?: "Unknown error occurred")
        }
    }
}