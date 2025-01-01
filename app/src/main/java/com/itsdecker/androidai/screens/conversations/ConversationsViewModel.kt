package com.itsdecker.androidai.screens.conversations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    chatRepository: ChatRepository,
    private val navigator: Navigator,
) : ViewModel() {

    val conversations = chatRepository
        .getAllConversations(apiKeyId = null)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = listOf(),
        )

    val apiKeys: StateFlow<List<ApiKeyEntity>> = chatRepository.getAllApiKeys()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun goToConversation(conversationId: String, apiKeyId: String) {
        navigator.navigateUptTo(
            NavRoute.Chat(
                apiKeyId = apiKeyId,
                conversationId = conversationId,
            )
        )
    }

    fun startNewConversation() = viewModelScope.launch {
        navigator.navigateUptTo(
            NavRoute.Chat(
                apiKeyId = null,
                conversationId = null,
            )
        )
    }

    fun goToAddKey() = navigator.navigateTo(NavRoute.ApiKeyForm(apiKeyId = null))

    fun goToKeys() = navigator.navigateTo(NavRoute.ApiKeys)

    fun goBack() = navigator.goBack()
}