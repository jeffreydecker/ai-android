package com.itsdecker.androidai.screens.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsdecker.androidai.navigation.NavigationArgs
import com.itsdecker.androidai.network.CLAUDE_EXPLORATION
import com.itsdecker.androidai.network.claude.ANTHROPIC_MESSENGER_ROLE_ASSISTANT
import com.itsdecker.androidai.network.claude.ANTHROPIC_MESSENGER_ROLE_USER
import com.itsdecker.androidai.network.claude.ClaudeApiClient
import com.itsdecker.androidai.network.claude.ClaudeApiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO - Without Hilt we need a view model factory to pass params like the
//  repository, potentially the api client and nav controller

class ChatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val apiClient = ClaudeApiClient(CLAUDE_EXPLORATION)

    private val conversationId = savedStateHandle.get<String>(NavigationArgs.CONVERSATION_ID_STRING)

    private val _conversation = MutableStateFlow(Conversation(emptyList()))
    val conversation = _conversation.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<ClaudeApiError?>(null)
    val error = _error.asStateFlow()

    init {
        Log.d("Decker Debug", "ChatViewModel got conversationId: $conversationId")
    }

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val userMessage = ChatMessage(ANTHROPIC_MESSENGER_ROLE_USER, prompt)
                updateConversation(userMessage)
                val result = apiClient.sendMessage(_conversation.value.messages)
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