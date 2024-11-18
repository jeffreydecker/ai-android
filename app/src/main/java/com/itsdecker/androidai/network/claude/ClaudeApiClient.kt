package com.itsdecker.androidai.network.claude

import com.google.gson.Gson
import com.itsdecker.androidai.screens.chat.ChatMessage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

// Claude Api wrapper for clean api access
class ClaudeApiClient(private val apiKey: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(ANTHROPIC_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
                // TODO - Not sure what's up with build config
//                level = if (BuildConfig.DEBUG) {
//                    HttpLoggingInterceptor.Level.BODY
//                } else {
//                    HttpLoggingInterceptor.Level.NONE
//                }
            })
            .build())
        .build()

    private val api = retrofit.create(ClaudeApi::class.java)

    suspend fun sendMessage(conversationHistory: List<ChatMessage>): String {
        val request = ClaudeRequest(
            messages = conversationHistory.map {
                Message(it.role, it.content)
            }
        )

        return try {
            val response = api.sendMessage(apiKey, request = request)
            response.content.firstOrNull()?.text ?: throw ClaudeApiError.UnknownError("Empty response")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)
            } catch (e: Exception) {
                null
            }
            throw ClaudeApiError.errorForCode(e.code(), errorResponse)
        } catch (e: IOException) {
            throw ClaudeApiError.NetworkError("Network error: ${e.message}")
        } catch (e: Exception) {
            throw ClaudeApiError.UnknownError(e.message ?: "Unknown error occurred")
        }
    }
}