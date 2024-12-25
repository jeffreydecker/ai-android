package com.itsdecker.androidai.data

sealed class ModelField(val name: String, val details: String) {

    companion object {
        val DEFAULT_FIELDS = listOf(
            Text.Name(""),
            Text.Description(""),
            Text.ApiKey(""),
        )
    }

    sealed class Text(open var value: String, name: String, details: String): ModelField(name, details) {

        data class Name(override var value: String) : Text(
            value = value,
            name = "Name",
            details = "The name of this model",
        )

        data class Description(override var value: String) : Text(
            value = value,
            name = "Description",
            details = "Add a brief description of what this model is for",
        )

        data class ApiKey(override var value: String) : Text(
            value = value,
            name = "Api Key",
            details = "The API key needed to access this model. Note this will only ever be stored locally.",
        )
    }
}