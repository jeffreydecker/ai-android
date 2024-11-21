package com.itsdecker.androidai.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.booleanResource
import com.itsdecker.androidai.R
import com.itsdecker.androidai.ui.theme.Claude
import com.itsdecker.androidai.ui.theme.Gemini
import com.itsdecker.androidai.ui.theme.Gpt

enum class SupportedModel(
    val modelName: String,
    @DrawableRes val icon: Int,
    val brandColor: Color,
    val fields: List<ModelField>,
    // TODO - Configurable fields
) {
    CLAUDE(
        modelName = "Claude",
        icon = R.drawable.ic_ai_claude,
        brandColor = Claude,
        fields = listOf(
            ModelField.Text.Name(""),
            ModelField.Text.Description(""),
            ModelField.Text.ApiKey(""),
        ),
    ),

    GPT(
        modelName = "GPT - Coming Soon",
        brandColor = Gpt,
        icon = R.drawable.ic_ai_gpt,
        fields = listOf(),
    ),

    GEMINI(
        modelName = "Gemini - Coming Soon",
        icon = R.drawable.ic_ai_gemini,
        brandColor = Gemini,
        fields = listOf(),
    ),

    UNKNOWN(
        modelName = "UNKOWN",
        icon = R.drawable.ic_round_error_outline,
        brandColor = Color.Red,
        fields = listOf(),
    )
}