package com.itsdecker.androidai.data

import androidx.annotation.DrawableRes
import com.itsdecker.androidai.R

enum class SupportedModel(
    val modelName: String,
    @DrawableRes val icon: Int,
    val fields: List<ModelField>,
) {
    CLAUDE(
        modelName = "Claude",
        icon = R.drawable.ic_ai_claude,
        fields = listOf(
            ModelField.Text.Name(""),
            ModelField.Text.Description(""),
            ModelField.Text.ApiKey(""),
        ),
    ),

    GPT(
        modelName = "GPT - Coming Soon",
        icon = R.drawable.ic_ai_claude,
        fields = listOf(),
    ),

    GEMINI(
        modelName = "Gemini - Coming Soon",
        icon = R.drawable.ic_ai_claude,
        fields = listOf(),
    ),

    UNKNOWN(
        modelName = "UNKOWN",
        icon = R.drawable.ic_ai_claude,
        fields = listOf(),
    )
}