package com.itsdecker.androidai.screens.conversations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.data.respository.SettingsRepository
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationEntity
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
    private val chatRepository: ChatRepository,
    private val navigator: Navigator,
    settingsRepository: SettingsRepository,
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

    val defaultApiKeyId: StateFlow<String?> = settingsRepository.defaultApiKeyId()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = null,
        )

    fun goToConversation(conversationId: String, apiKeyId: String?) {
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

    fun editApiKey(apiKeyEntity: ApiKeyEntity) =
        navigator.navigateTo(NavRoute.ApiKeyForm(apiKeyId = apiKeyEntity.id))

    fun updateChatKey(conversation: ConversationEntity, apiKeyEntity: ApiKeyEntity) {
        viewModelScope.launch {
            chatRepository.updateConversation(conversation.copy(apiKeyId = apiKeyEntity.id))
        }
    }

    fun updateChatName(conversation: ConversationEntity, name: String) {
        viewModelScope.launch {
            chatRepository.updateConversation(conversation.copy(title = name))
        }
    }

    fun deleteChat(conversation: ConversationEntity) = viewModelScope.launch {
        chatRepository.deleteConversation(conversation)
    }
}