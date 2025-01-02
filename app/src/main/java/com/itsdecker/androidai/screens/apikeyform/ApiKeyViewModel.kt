package com.itsdecker.androidai.screens.apikeyform

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.itsdecker.androidai.R
import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.data.respository.SettingsRepository
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiKeyViewModel @Inject constructor(
    private val navigator: Navigator,
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<NavRoute.ApiKeyForm>()

    private val isNewKey = route.apiKeyId.isNullOrEmpty()

    private val _apiKeyEntity = MutableStateFlow(ApiKeyEntity.empty())
    val apiKeyEntity = _apiKeyEntity.asStateFlow()

    private val _isDefaultKey = MutableStateFlow(false)
    val isDefaultKey = _isDefaultKey.asStateFlow()

    private val _userAgreementChecked = MutableStateFlow(!isNewKey)
    val userAgreementChecked = _userAgreementChecked.asStateFlow()

    init {
        viewModelScope.launch {
            route.apiKeyId?.let { apiKeyId ->
                // Existing key
                chatRepository.getApiKey(apiKeyId)?.let { _apiKeyEntity.value = it }
                    ?: throw IllegalStateException("Api key not found")

                _isDefaultKey.value = settingsRepository.getDefaultApiKeyId() == apiKeyId
            } ?: run {
                // New key and no default key set
                _isDefaultKey.value = settingsRepository.getDefaultApiKeyId() == null
            }
        }
    }

    fun onProviderSelected(provider: SupportedProvider, context: Context) {
        var updatedKey = _apiKeyEntity.value.copy(chatModel = provider)

        if (isNewKey) {
            updatedKey = updatedKey.copy(
                name = context.getString(R.string.api_key_name_default, provider.name),
                description = context.getString(R.string.api_key_description_default, provider.name),
            )
        }

        _apiKeyEntity.value = updatedKey
    }

    fun onNameChanged(name: String) {
        _apiKeyEntity.value = _apiKeyEntity.value.copy(
            name = name
        )
    }

    fun onDescriptionChanged(description: String) {
        _apiKeyEntity.value = _apiKeyEntity.value.copy(
            description = description
        )
    }

    fun onApiKeyChanged(apiKey: String) {
        _apiKeyEntity.value = _apiKeyEntity.value.copy(
            apiKey = apiKey
        )
    }

    fun onDefaultKeyChanged(isDefault: Boolean) {
        _isDefaultKey.value = isDefault
    }

    fun onUserAgreementChanged(isChecked: Boolean) {
        _userAgreementChecked.value = isChecked
    }

    fun saveModel() {
        if (!canSave()) return

        viewModelScope.launch {
            when(isNewKey) {
                true -> chatRepository.createApiKey(apiKey = _apiKeyEntity.value)
                false -> chatRepository.updateApiKey(apiKey = _apiKeyEntity.value)
            }
            if (_isDefaultKey.value) settingsRepository.setDefaultApiKeyId(_apiKeyEntity.value.id)
            navigator.goBack()
        }
    }

    fun cancel() = navigator.goBack()

    fun deleteModel() {
        if (!isExistingKey()) return

        viewModelScope.launch {
            async {
                chatRepository.deleteApiKey(apiKey = _apiKeyEntity.value)
                // If this is our default API key, clear it
                if (settingsRepository.getDefaultApiKeyId() == _apiKeyEntity.value.id) {
                    settingsRepository.setDefaultApiKeyId(null)
                }
            }.await()
            navigator.goBack()
        }
    }

    fun canSave(): Boolean = _apiKeyEntity.value.chatModel != SupportedProvider.UNINITIALIZED
            && _apiKeyEntity.value.name.isNotBlank()
            && _apiKeyEntity.value.description.isNotBlank()
            && _apiKeyEntity.value.apiKey.isNotBlank()
            && _userAgreementChecked.value

    fun isExistingKey(): Boolean = !isNewKey
}