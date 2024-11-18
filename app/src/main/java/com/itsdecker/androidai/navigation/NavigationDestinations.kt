package com.itsdecker.androidai.navigation

import kotlinx.serialization.Serializable

sealed class NavigationDestination {

    @Serializable
    data object Main : NavigationDestination()

    @Serializable
    data object AddModel : NavigationDestination()

    @Serializable
    data class Chat(val conversationId: String = "placeholder") : NavigationDestination()

    @Serializable
    data class Secondary(val selectedModel: String) : NavigationDestination()
}