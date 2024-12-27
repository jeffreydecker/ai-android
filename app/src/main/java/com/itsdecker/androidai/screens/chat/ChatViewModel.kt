package com.itsdecker.androidai.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_ASSISTANT
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_USER
import com.itsdecker.androidai.network.anthropic.AnthropicApiClient
import com.itsdecker.androidai.network.anthropic.AnthropicApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val apiClient: AnthropicApiClient,
) : ViewModel() {


    private val chatRoute = savedStateHandle.toRoute<NavRoute.Chat>()
    private lateinit var apiKey: ApiKeyEntity

    val conversation: StateFlow<ConversationWithMessages?> =
        chatRepository
            .getConversation(chatRoute.conversationId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = null,
            )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<AnthropicApiError?>(null)
    val error = _error.asStateFlow()

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            apiKey = chatRepository.getChatModel(apiKeyId = chatRoute.apiKeyId)
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            conversation.value?.let { cwm ->
                try {
                    val userMessage = MessageEntity(
                        role = ANTHROPIC_MESSENGER_ROLE_USER,
                        content = message,
                        conversationId = cwm.conversation.id,
                    )
                    updateConversation(message = userMessage)
                    val result = apiClient.sendMessage(
                        apiKey = apiKey.apiKey,
                        conversationHistory = cwm.messages
                            .toMutableList()
                            .apply {
                                add(userMessage)
                            },
                    )
                    updateConversation(
                        message = MessageEntity(
                            role = ANTHROPIC_MESSENGER_ROLE_ASSISTANT,
                            content = result,
                            conversationId = cwm.conversation.id,
                        ),
                    )
                } catch (e: AnthropicApiError) {
                    _error.value = e
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    private fun updateConversation(
        message: MessageEntity
    ) {
        conversation.value?.let { cwm ->
            viewModelScope.launch {
                chatRepository.addMessage(message = message)
            }
        }
    }

    fun createNewConversation() {
        // TODO - Use this with a "New Conversation" button
    }
}