package com.itsdecker.androidai.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.itsdecker.androidai.R
import com.itsdecker.androidai.ui.theme.Claude
import com.itsdecker.androidai.ui.theme.Gemini
import com.itsdecker.androidai.ui.theme.Gpt

val SUPPORTED_PROVIDERS = listOf(
    SupportedProvider.Anthropic,
)

enum class SupportedProvider(
    val providerName: String,
    @DrawableRes val icon: Int,
    val brandColor: Color,
) {
    Anthropic(
        providerName = "Anthropic",
        icon = R.drawable.ic_ai_claude,
        brandColor = Claude,
    ),

    OpenAI(
        providerName = "OpenAI - Coming Soon",
        brandColor = Gpt,
        icon = R.drawable.ic_ai_gpt,
    ),

    Google(
        providerName = "Google - Coming Soon",
        icon = R.drawable.ic_ai_gemini,
        brandColor = Gemini,
    ),

    UNKNOWN(
        providerName = "UNKOWN",
        icon = R.drawable.ic_round_error_outline,
        brandColor = Color.Red,
    )
}