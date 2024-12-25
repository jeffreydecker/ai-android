package com.itsdecker.androidai.screens.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.database.ChatModelEntity
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.NavigationArgs
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_ASSISTANT
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_USER
import com.itsdecker.androidai.network.anthropic.ClaudeApiClient
import com.itsdecker.androidai.network.anthropic.ClaudeApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepo: ChatRepository,
    private val apiClient: ClaudeApiClient,
) : ViewModel() {


    private val chatRoute = savedStateHandle.toRoute<NavRoute.Chat>()
    private lateinit var apiKey: ChatModelEntity

    private val _conversation = MutableStateFlow(Conversation(emptyList()))
    val conversation: StateFlow<Conversation> = _conversation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<ClaudeApiError?>(null)
    val error = _error.asStateFlow()

    init {
        Log.d("Decker Debug", "ChatViewModel got conversationId: ${chatRoute.conversationId}")

        viewModelScope.launch(context = Dispatchers.IO) {
            apiKey = chatRepo.getChatModel(chatModelId = chatRoute.apiKeyId)
        }

        chatRoute.conversationId?.let { conversationId ->
            viewModelScope.launch {
                // TODO - Something like this I think
                //  Will need more setup, so essentially, insert a new conversation if it's a new one
                //  but I think that should happen when we tap the add button
                // conversation = chatRepo.getConversation(conversationId).stateIn(scope = viewModelScope)
            }
        }


    }

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val userMessage = ChatMessage(ANTHROPIC_MESSENGER_ROLE_USER, prompt)
                updateConversation(userMessage)
                val result = apiClient.sendMessage(
                    apiKey = apiKey.apiKey,
                    conversationHistory = _conversation.value.messages,
                )
                val assistantMessage = ChatMessage(ANTHROPIC_MESSENGER_ROLE_ASSISTANT, result)
                updateConversation(assistantMessage)
            } catch (e: ClaudeApiError) {
                _error.value = e
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateConversation(message: ChatMessage) {
        _conversation.update { current ->
            current.copy(messages = current.messages + message)
        }
    }

    // TODO - Use this with a "New Conversation" button
    fun clearConversation() {
        _conversation.value = Conversation(emptyList())
    }
}