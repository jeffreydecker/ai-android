package com.itsdecker.androidai.screens.main

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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigator: Navigator,
    private val chatRepository: ChatRepository,
) : ViewModel() {

    val chatModels: StateFlow<List<ApiKeyEntity>> = chatRepository.getAllApiKeys()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun goToChat(apiKeyId: String) = navigator.navigateTo(NavRoute.Conversations(apiKeyId = apiKeyId))
    fun goToAddModel() = navigator.navigateTo(NavRoute.AddKey)
}