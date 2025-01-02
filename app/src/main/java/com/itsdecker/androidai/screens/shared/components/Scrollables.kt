package com.itsdecker.androidai.screens.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun ScrollableContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        content()

        TopToBottomGradient(
            height = spacing.medium,
            color = colorScheme.surface,
        )

        BottomToTopGradient(
            modifier = Modifier.align(Alignment.BottomCenter),
            height = spacing.medium,
            color = colorScheme.surface,
        )
    }
}

