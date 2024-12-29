package com.itsdecker.androidai.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.data.respository.SettingsRepository
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.Navigator
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_ASSISTANT
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_USER
import com.itsdecker.androidai.network.anthropic.AnthropicApiClient
import com.itsdecker.androidai.network.anthropic.AnthropicApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val apiClient: AnthropicApiClient,
    private val chatRepository: ChatRepository,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val chatRoute = savedStateHandle.toRoute<NavRoute.Chat>()

    private var _conversation = MutableStateFlow<ConversationWithMessages?>(null)
    var conversation = _conversation.asStateFlow()

    private val _selectedApiKey = MutableStateFlow<ApiKeyEntity?>(null)
    val selectedApiKey = _selectedApiKey.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<AnthropicApiError?>(null)
    val error = _error.asStateFlow()

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            when (chatRoute.conversationId) {
                null -> {
                    _selectedApiKey.value =
                        chatRepository.getApiKey(apiKeyId = apiKeyIdFromRouteOrDefault())
                }

                else -> {
                    _conversation.value = chatRepository
                        .getConversation(conversationId = chatRoute.conversationId)
                        .also {
                            val apiKeyId =
                                it?.conversation?.apiKeyId ?: apiKeyIdFromRouteOrDefault()
                            _selectedApiKey.value = chatRepository.getApiKey(apiKeyId = apiKeyId)
                        }
                }
            }
        }
    }

    fun goToAddApiKey() {
        navigator.navigateTo(NavRoute.AddKey)
    }

    fun goToApiKeySettings() {
        navigator.navigateTo(NavRoute.ApiKeys)
    }

    fun goToConversations() {
        navigator.navigateTo(NavRoute.Conversations())
    }

    fun updateSelectedApiKey(selectedApiKey: ApiKeyEntity) {
        _selectedApiKey.value = selectedApiKey
    }

    fun sendMessage(message: String) {
        _selectedApiKey.value?.let { apiKey ->
            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null

                val conversation = _conversation.value
                    ?: createNewConversation(message = message, apiKey = apiKey)

                try {
                    val userMessage = MessageEntity(
                        role = ANTHROPIC_MESSENGER_ROLE_USER,
                        content = message,
                        conversationId = conversation.conversation.id,
                    )

                    updateConversation(message = userMessage)

                    val assistantResult = apiClient.sendMessage(
                        apiKey = apiKey.apiKey,
                        conversationHistory = conversation.messages
                            .toMutableList()
                            .apply {
                                add(userMessage)
                            },
                    ).let {
                        MessageEntity(
                            role = ANTHROPIC_MESSENGER_ROLE_ASSISTANT,
                            content = it,
                            conversationId = conversation.conversation.id,
                        )
                    }

                    updateConversation(message = assistantResult)
                } catch (e: AnthropicApiError) {
                    _error.value = e
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    // Update local instance and database with new message
    private fun updateConversation(message: MessageEntity) {
        _conversation.value?.let { conversation ->
            // Update local instance
            _conversation.value = conversation.copy(
                messages = conversation.messages.toMutableList().apply { add(message) }
            )
            // Update database
            viewModelScope.launch {
                chatRepository.addMessage(message = message)
            }
        }
    }

    private suspend fun createNewConversation(
        message: String,
        apiKey: ApiKeyEntity
    ): ConversationWithMessages {
        val initialTitle = when (message.length > 32) {
            true -> message.take(29) + "..."
            false -> message
        }

        return chatRepository.createConversation(
            apiKeyId = apiKey.id,
            title = initialTitle,
        ).also {
            _conversation.value = it
        }
    }

    private suspend fun apiKeyIdFromRouteOrDefault() =
        chatRoute.apiKeyId ?: settingsRepository.getDefaultApiKeyId()
}