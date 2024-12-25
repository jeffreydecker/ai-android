package com.itsdecker.androidai.screens.addkey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsdecker.androidai.data.ModelField
import com.itsdecker.androidai.data.ModelField.Companion.DEFAULT_FIELDS
import com.itsdecker.androidai.data.SUPPORTED_PROVIDERS
import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.data.respository.ChatRepository
import com.itsdecker.androidai.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddKeyViewModel @Inject constructor(
    private val navigator: Navigator,
    private val chatRepo: ChatRepository,
) : ViewModel() {

    val supportedProviders = SupportedProvider.entries.filter { it != SupportedProvider.UNKNOWN }

    private val _modelSelection = MutableStateFlow<SupportedProvider?>(null)
    val modelSelection = _modelSelection.asStateFlow()

    private val formFields = mutableListOf<ModelField>()
    private val completedFormFields = mutableListOf<ModelField>()

    private val _currentField = MutableStateFlow<ModelField?>(null)
    val currentField = _currentField.asStateFlow()

    fun setProvider(model: SupportedProvider) {
        if (!SUPPORTED_PROVIDERS.contains(model)) return

        _modelSelection.value = model
        val modelFields = DEFAULT_FIELDS.toMutableList()
        _currentField.value = modelFields.removeFirstOrNull()
        formFields.apply {
            clear()
            addAll(modelFields)
        }
        completedFormFields.clear()
    }

    fun submitCurrentField() {
        val nextField = formFields.removeFirstOrNull()
        _currentField.value?.let { currentFormField -> completedFormFields.add(currentFormField) }
        _currentField.value = nextField
    }

    fun goBack() {
        val previousField = completedFormFields.removeLastOrNull()
        _currentField.value?.let { currentFormField -> formFields.add(0, currentFormField) }
        _currentField.value = previousField
        if (previousField == null) {
            _modelSelection.value = null
        }
    }

    fun updateTextValue(textValue: String) {
        (_currentField.value as? ModelField.Text)?.let { fieldValue ->
            _currentField.value = when (fieldValue) {
                is ModelField.Text.ApiKey -> fieldValue.copy(value = textValue)
                is ModelField.Text.Description -> fieldValue.copy(value = textValue)
                is ModelField.Text.Name -> fieldValue.copy(value = textValue)
            }
        }
    }

    fun saveModel() {
        viewModelScope.launch {
            _modelSelection.value?.let {
                async {
                    chatRepo.createApiKey(
                        name = completedFormFields.firstOfType<ModelField.Text.Name>()?.value ?: "",
                        description = completedFormFields.firstOfType<ModelField.Text.Description>()?.value
                            ?: "",
                        apiKey = completedFormFields.firstOfType<ModelField.Text.ApiKey>()?.value
                            ?: "",
                        supportedProvider = it,
                    )
                }.await()
            }
            navigator.goBack()
        }
    }

    private inline fun <reified T> List<Any>.firstOfType(): T? = firstOrNull { it is T } as? T
}