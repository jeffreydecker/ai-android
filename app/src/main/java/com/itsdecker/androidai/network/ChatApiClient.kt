package com.itsdecker.androidai.network

import com.google.gson.Gson
import com.itsdecker.androidai.BuildConfig
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.ChatRole.Companion.toRole
import com.itsdecker.androidai.network.deepseek.DEEP_SEEK_BASE_URL
import com.itsdecker.androidai.network.deepseek.DEEP_SEEK_ROLE_ASSISTANT
import com.itsdecker.androidai.network.deepseek.DEEP_SEEK_ROLE_USER
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

abstract class ChatApiClient constructor(
    baseUrl: String,
) {

    abstract suspend fun internalSendMessage(
        apiKey: String,
        conversationId: String,
        conversationHistory: List<MessageEntity>,
    ) : MessageEntity?

    internal val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }).build()
        ).build()

    suspend fun sendMessage(
        apiKey: String,
        conversationId: String,
        conversationHistory: List<MessageEntity>,
    ): MessageEntity {
        return try {
            internalSendMessage(
                apiKey = apiKey,
                conversationId = conversationId,
                conversationHistory = conversationHistory,
            ) ?: throw ChatApiError.UnknownError("Empty response")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java)
            } catch (e: Exception) {
                null
            }
            throw ChatApiError.errorForCode(e.code(), errorResponse)
        } catch (e: IOException) {
            throw ChatApiError.NetworkError("Network error: ${e.message}")
        } catch (e: Exception) {
            throw ChatApiError.UnknownError(e.message ?: "Unknown error occurred")
        }
    }

    private fun String.toDeepSeekRole() = when (this.toRole()) {
        ChatRole.Assistant -> DEEP_SEEK_ROLE_ASSISTANT
        ChatRole.User -> DEEP_SEEK_ROLE_USER
    }
}