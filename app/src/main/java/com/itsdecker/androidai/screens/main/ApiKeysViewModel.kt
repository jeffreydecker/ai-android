package com.itsdecker.androidai.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.data.respository.SettingsRepository
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiKeysViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val navigator: Navigator,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val apiKeys: StateFlow<List<ApiKeyEntity>> = chatRepository.getAllApiKeys()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _defaultApiKeyId = MutableStateFlow<String?>(null)
    val defaultApiKeyId = _defaultApiKeyId.asStateFlow()

    init {
        viewModelScope.launch {
            _defaultApiKeyId.value = settingsRepository.getDefaultApiKeyId()
        }
    }

    fun setKeyAsDefault(apiKey: ApiKeyEntity) = viewModelScope.launch {
        settingsRepository.setDefaultApiKeyId(apiKeyId = apiKey.id)
        _defaultApiKeyId.value = apiKey.id
    }

    fun goToChat(apiKeyId: String) = navigator.navigateTo(NavRoute.Conversations(apiKeyId = apiKeyId))
    fun goToAddModel() = navigator.navigateTo(NavRoute.ApiKeyForm(apiKeyId = null))
}