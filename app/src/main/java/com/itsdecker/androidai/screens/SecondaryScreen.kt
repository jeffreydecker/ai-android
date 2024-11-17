package com.itsdecker.androidai.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.itsdecker.androidai.ui.theme.Typography

@Preview
@Composable
fun SecondaryScreen(
    selectedModel: String = "",
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "You selected $selectedModel",
            style = Typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
        )
    }

}