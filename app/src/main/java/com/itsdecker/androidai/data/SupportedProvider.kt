package com.itsdecker.androidai.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.itsdecker.androidai.R
import com.itsdecker.androidai.ui.theme.ClaudeBrand
import com.itsdecker.androidai.ui.theme.DeepSeekBrand
import com.itsdecker.androidai.ui.theme.GeminiBrand
import com.itsdecker.androidai.ui.theme.GptBrand

// Icons - https://lobehub.com/icons
// Brand Colors - https://lobehub.com/icons and https://www.brandcolorcode.com/

val SUPPORTED_PROVIDERS = listOf(
    SupportedProvider.Anthropic,
    SupportedProvider.DeepSeek,
)

enum class SupportedProvider(
    val providerName: String,
    @DrawableRes val icon: Int,
    val brandColor: Color,
) {
    Anthropic(
        providerName = "Anthropic Claude",
        icon = R.drawable.ic_ai_claude,
        brandColor = ClaudeBrand,
    ),

    DeepSeek(
        providerName = "DeepSeek",
        icon = R.drawable.ic_ai_deep_seek,
        brandColor = DeepSeekBrand,
    ),

    OpenAI(
        providerName = "OpenAI ChatGPT",
        icon = R.drawable.ic_ai_open_ai,
        brandColor = GptBrand,
    ),

    Google(
        providerName = "Google Gemini",
        icon = R.drawable.ic_ai_gemini,
        brandColor = GeminiBrand,
    ),

    UNINITIALIZED(
        providerName = "",
        icon = R.drawable.ic_round_error_outline,
        brandColor = Color.Red,
    ),

    UNKNOWN(
        providerName = "UNKOWN",
        icon = R.drawable.ic_round_error_outline,
        brandColor = Color.Red,
    )
}