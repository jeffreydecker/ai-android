package com.itsdecker.androidai.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun FormField(
    title: String,
    details: String? = null,
    fieldView: (@Composable ColumnScope.() -> Unit)? = null,
    subContentView: @Composable (BoxScope.() -> Unit)? = null,
    trailingContentView: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        Modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(all = spacing.default),
            verticalArrangement = Arrangement.spacedBy(spacing.default),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = spacing.small)
            ) {
                Text(
                    text = title,
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                )

                details?.let {
                    Text(
                        text = details,
                        style = Typography.bodySmall,
                        color = colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            fieldView?.let { fieldView() }

            Box(
                modifier = Modifier.padding(horizontal = spacing.small)
            ) {
                subContentView?.let { subContentView() }
            }
        }

        trailingContentView?.let { trailingContentView() }
    }
}

@Composable
fun FormTextInput(
    modifier: Modifier = Modifier,
    text: String,
    obfuscate: Boolean = false,
    enabled: Boolean = true,
    maxLines: Int = 1,
    maxCharacters: Int = Int.MAX_VALUE,
    onValueChanged: (text: String) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { newValue -> onValueChanged(newValue.take(maxCharacters)) },
            singleLine = maxLines == 1,
            maxLines = maxLines,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (obfuscate) {
                PasswordVisualTransformation()
            } else VisualTransformation.None,
            enabled = enabled,
        )
        if (maxCharacters < Int.MAX_VALUE) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.tiny),
                text = "${text.length}/$maxCharacters",
                color = when (text.length >= maxCharacters) {
                    true -> colorScheme.error
                    false -> colorScheme.onSurfaceVariant
                },
                style = Typography.labelMedium,
                textAlign = TextAlign.End,
            )
        }
    }
}


@Composable
fun FormSlider() {

}

@Composable
fun FormSubcontentText(
    text: String,
    style: TextStyle = Typography.bodyLarge,
) {
    Text(
        text = text,
        style = style,
        color = colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
    )
}