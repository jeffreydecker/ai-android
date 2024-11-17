package com.itsdecker.androidai.screens.addmodel

import androidx.lifecycle.ViewModel
import com.itsdecker.androidai.data.ModelField
import com.itsdecker.androidai.data.SupportedModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddModelViewModel() : ViewModel() {

    private val _modelSelection = MutableStateFlow<SupportedModel?>(null)
    val modelSelection = _modelSelection.asStateFlow()

    private val formFields = mutableListOf<ModelField>()
    private val completedFormFields = mutableListOf<ModelField>()

    private val _currentField = MutableStateFlow<ModelField?>(null)
    val currentField = _currentField.asStateFlow()

    fun setModel(model: SupportedModel) {
        _modelSelection.value = model
        val modelFields = model.fields.toMutableList()
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
        _currentField.value?.let {currentFormField -> formFields.add(0, currentFormField) }
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
        // TODO
    }
}