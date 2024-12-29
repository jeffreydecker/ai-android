package com.itsdecker.androidai.screens.conversations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.data.respository.SettingsRepository
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val navRoute = savedStateHandle.toRoute<NavRoute.Conversations>()

    val conversations = chatRepository
        .getAllConversations(apiKeyId = navRoute.apiKeyId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = listOf(),
        )

    fun goToConversation(conversationId: String, apiKeyId: String) {
        navigator.navigateTo(
            NavRoute.Chat(
                apiKeyId = apiKeyId,
                conversationId = conversationId,
            )
        )
    }

    fun startNewConversation() = viewModelScope.launch {
        val apiKeyId = navRoute.apiKeyId ?: settingsRepository.getDefaultApiKeyId()
        apiKeyId?.let {
            chatRepository.createConversation(
                apiKeyId = it,
            ).let { conversation ->
                goToConversation(
                    conversationId = conversation.conversation.id,
                    apiKeyId = apiKeyId,
                )
            }
        }
    }
}