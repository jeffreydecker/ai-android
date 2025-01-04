package com.itsdecker.androidai.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.itsdecker.androidai.R
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_CLAUDE_3_5_MODEL_HAIKU
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_CLAUDE_3_5_MODEL_SONNET
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_CLAUDE_3_MODEL_OPUS
import com.itsdecker.androidai.network.deepseek.DEEP_SEEK_CHAT
import com.itsdecker.androidai.network.openai.OPEN_AI_GPT_4o
import com.itsdecker.androidai.network.openai.OPEN_AI_GPT_4o_MINI
import com.itsdecker.androidai.network.openai.OPEN_AI_GPT_o1
import com.itsdecker.androidai.network.openai.OPEN_AI_GPT_o1_MINI
import com.itsdecker.androidai.ui.theme.ClaudeBrand
import com.itsdecker.androidai.ui.theme.DeepSeekBrand
import com.itsdecker.androidai.ui.theme.GeminiBrand
import com.itsdecker.androidai.ui.theme.GptBrand
import com.itsdecker.androidai.ui.theme.OpenRouterBrand

// Icons - https://lobehub.com/icons
// Brand Colors - https://lobehub.com/icons and https://www.brandcolorcode.com/

val SUPPORTED_PROVIDERS = listOf(
    SupportedProvider.Anthropic,
    SupportedProvider.OpenAI,
    SupportedProvider.DeepSeek,
    SupportedProvider.Google,
    SupportedProvider.OpenRouter,
)

enum class SupportedProvider(
    val providerName: String,
    @DrawableRes val icon: Int,
    val brandColor: Color,
    val apiKeyLink: String,
    val models: List<SupportedModel>,
) {
    Anthropic(
        providerName = "Anthropic Claude",
        icon = R.drawable.ic_ai_claude,
        brandColor = ClaudeBrand,
        apiKeyLink = "https://console.anthropic.com/settings/keys",
        models = listOf(
            SupportedModel(
                name = "Claude 3.5 Haiku",
                value = ANTHROPIC_CLAUDE_3_5_MODEL_HAIKU,
            ),
            SupportedModel(
                name = "Claude 3.5 Sonnet",
                value = ANTHROPIC_CLAUDE_3_5_MODEL_SONNET,
                isDefault = true,
            ),
            SupportedModel(
                name = "Claude 3 Opus",
                value = ANTHROPIC_CLAUDE_3_MODEL_OPUS,
            ),
        ),
    ),

    DeepSeek(
        providerName = "DeepSeek",
        icon = R.drawable.ic_ai_deep_seek,
        brandColor = DeepSeekBrand,
        apiKeyLink = "https://platform.deepseek.com/api_keys",
        models = listOf(
            SupportedModel(
                name = "DeepSeek",
                value = DEEP_SEEK_CHAT,
                isDefault = true,
            ),
        ),
    ),

    OpenAI(
        providerName = "OpenAI ChatGPT",
        icon = R.drawable.ic_ai_open_ai,
        brandColor = GptBrand,
        apiKeyLink = "https://platform.openai.com/settings/organization/api-keys",
        models = listOf(
            SupportedModel(
                name = "GPT-4o mini",
                value = OPEN_AI_GPT_4o_MINI,
            ),
            SupportedModel(
                name = "GPT-4o",
                value = OPEN_AI_GPT_4o,
            ),
            SupportedModel(
                name = "o1 mini",
                value = OPEN_AI_GPT_o1_MINI,
                isDefault = true,
            ),
            SupportedModel(
                name = "o1",
                value = OPEN_AI_GPT_o1,
                isDefault = true,
            ),
        ),
    ),

    OpenRouter(
        providerName = "OpenRouter",
        icon = R.drawable.ic_ai_open_router,
        brandColor = OpenRouterBrand,
        apiKeyLink = "https://openrouter.ai/settings/keys",
        models = listOf(),
    ),

    Google(
        providerName = "Google Gemini",
        icon = R.drawable.ic_ai_gemini,
        brandColor = GeminiBrand,
        apiKeyLink = "",
        models = listOf(),
    ),

    UNINITIALIZED(
        providerName = "",
        icon = R.drawable.ic_round_error_outline,
        brandColor = Color.Red,
        apiKeyLink = "",
        models = listOf(),
    ),

    UNKNOWN(
        providerName = "UNKOWN",
        icon = R.drawable.ic_round_error_outline,
        brandColor = Color.Red,
        apiKeyLink = "",
        models = listOf(),
    )
}

data class SupportedModel(
    val name: String,
    val value: String,
    val apiPath: String? = null, // Needed for open router
    val isDefault: Boolean = false,
)