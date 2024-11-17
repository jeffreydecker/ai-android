package com.itsdecker.androidai.data

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import com.itsdecker.androidai.R

//sealed class SupportedModels {
//    data class Claude(val apiKey: ModelField.ApiKey)
//
//}

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
    )
}