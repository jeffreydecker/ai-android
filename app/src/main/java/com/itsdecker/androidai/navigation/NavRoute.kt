package com.itsdecker.androidai.navigation

import kotlinx.serialization.Serializable

sealed class NavRoute {

    @Serializable
    data class Chat(
        val apiKeyId: String?,
        val conversationId: String?,
    ) : NavRoute()

    @Serializable
    data class Conversations(
        val apiKeyId: String? = null,
    ) : NavRoute()

    @Serializable
    data object ApiKeys : NavRoute()

    @Serializable
    data class ApiKeyForm(
        val apiKeyId: String?
    ) : NavRoute()

}