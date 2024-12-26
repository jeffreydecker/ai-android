package com.itsdecker.androidai.navigation

import kotlinx.serialization.Serializable

sealed class NavRoute {

    @Serializable
    data object Main : NavRoute()

    @Serializable
    data object AddKey : NavRoute()

    @Serializable
    data class Chat(
        val apiKeyId: String,
        val conversationId: String? = null,
    ) : NavRoute()
}