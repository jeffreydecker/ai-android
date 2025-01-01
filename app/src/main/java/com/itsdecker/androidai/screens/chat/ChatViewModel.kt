package com.itsdecker.androidai.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.itsdecker.androidai.data.respository.ApiRepository
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.data.respository.SettingsRepository
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.Navigator
import com.itsdecker.androidai.network.ChatApiError
import com.itsdecker.androidai.network.ChatRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
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

    private val _error = MutableStateFlow<ChatApiError?>(null)
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
        navigator.navigateTo(NavRoute.ApiKeyForm(apiKeyId = null))
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
                        role = ChatRole.User.value,
                        content = message,
                        conversationId = conversation.conversation.id,
                    )

                    updateConversation(message = userMessage)

                    val assistantResult = apiRepository.sendMessage(
                        apiKey = apiKey,
                        conversationId = conversation.conversation.id,
                        conversationHistory = conversation.messages
                            .toMutableList()
                            .apply {
                                add(userMessage)
                            },
                    )

                    updateConversation(message = assistantResult)
                } catch (e: ChatApiError) {
                    _error.value = e
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    // Update local instance and database with new message
    // TODO - Maybe move this to ApiRepository?
    private fun updateConversation(message: MessageEntity) {
        _conversation.value?.let { conversation ->
            val updatedConversation = conversation.copy(
                conversation = conversation.conversation.copy(updatedAt = message.timestamp),
                messages = conversation.messages.toMutableList().apply { add(message) }
            )

            // Update local instance
            _conversation.value = updatedConversation

            // Update database
            viewModelScope.launch {
                chatRepository.addMessage(message = message)
                chatRepository.updateConversation(updatedConversation.conversation)
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