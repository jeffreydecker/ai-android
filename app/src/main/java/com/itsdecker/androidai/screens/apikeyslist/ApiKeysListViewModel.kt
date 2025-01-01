package com.itsdecker.androidai.screens.apikeyslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.data.respository.SettingsRepository
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
class ApiKeysListViewModel @Inject constructor(
    chatRepository: ChatRepository,
    private val navigator: Navigator,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val apiKeys: StateFlow<List<ApiKeyEntity>> = chatRepository.getAllApiKeys()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val defaultApiKeyId = settingsRepository.observeDefaultApiKey()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    init {
        viewModelScope.launch {
            // Used to initialize default API Key
            settingsRepository.maybeInitializeDefaultApiKey()
        }
    }

    fun goToEditKey(apiKeyId: String) = navigator.navigateTo(NavRoute.ApiKeyForm(apiKeyId = apiKeyId))

    fun goToAddKey() = navigator.navigateTo(NavRoute.ApiKeyForm(apiKeyId = null))

    fun goBack() = navigator.goBack()
}