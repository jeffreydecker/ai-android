package com.itsdecker.androidai.screens.shared.modifiers

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity

fun Modifier.dynamicNavigationBarPadding(): Modifier = composed {
    val density = LocalDensity.current
    var isKeyboardVisible by remember { mutableStateOf(false) }

    val navigationBarInsets = WindowInsets.navigationBars.only(
        WindowInsetsSides.Bottom
    )

    val navigationBarHeight = with(density) {
        navigationBarInsets.getBottom(density).toDp()
    }

    WindowInsets.Companion.ime.getBottom(density).let { keyboardHeight ->
        isKeyboardVisible = keyboardHeight > navigationBarInsets.getBottom(density)
    }

    if (!isKeyboardVisible) {
        this.padding(bottom = navigationBarHeight)
    } else {
        this
    }
}