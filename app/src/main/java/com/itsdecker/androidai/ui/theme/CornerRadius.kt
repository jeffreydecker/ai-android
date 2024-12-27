package com.itsdecker.androidai.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data object CornerRadiusDefaults {
    internal const val EXTRA_SMALL = 2
    internal const val SMALL = 4
    internal const val MEDIUM = 8
    internal const val LARGE = 16
    internal const val EXTRA_LARGE = 32
    internal const val DEFAULT = SMALL
}

data class CornerRadius(
    val default: Dp = CornerRadiusDefaults.DEFAULT.dp,
    val extraSmall: Dp = CornerRadiusDefaults.EXTRA_SMALL.dp,
    val small: Dp = CornerRadiusDefaults.SMALL.dp,
    val medium: Dp = CornerRadiusDefaults.MEDIUM.dp,
    val large: Dp = CornerRadiusDefaults.LARGE.dp,
    val extraLarge: Dp = CornerRadiusDefaults.EXTRA_LARGE.dp,
)