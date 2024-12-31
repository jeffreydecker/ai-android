package com.itsdecker.androidai.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun TopToBottomGradient(
    modifier: Modifier = Modifier,
    height: Dp,
    color: Color,
) {
    Gradient(
        modifier = modifier,
        height = height,
        colors = listOf(
            color,
            Color.Transparent,
        )
    )
}

@Composable
fun BottomToTopGradient(
    modifier: Modifier = Modifier,
    height: Dp,
    color: Color,
) {
    Gradient(
        modifier = modifier,
        height = height,
        colors = listOf(
            Color.Transparent,
            color,
        )
    )
}

@Composable
private fun Gradient(
    modifier: Modifier = Modifier,
    height: Dp,
    colors: List<Color>,
) {
    val gradientBrush = Brush.verticalGradient(
        colors = colors,
        startY = 0f,
        endY = Float.POSITIVE_INFINITY,
    )

    Box(
        modifier = modifier
            .background(brush = gradientBrush)
            .fillMaxWidth()
            .height(height = height),
    )
}