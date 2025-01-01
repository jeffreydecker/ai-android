package com.itsdecker.androidai.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.R
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun EmptyContentNotice(
    modifier: Modifier = Modifier,
    title: String,
    details: String,
    leadingView: (@Composable ColumnScope.() -> Unit)? = null,
    trailingView: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(spacing.default),
    ) {
        leadingView?.let { leadingView() }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = Typography.titleLarge,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = details,
            style = Typography.bodyLarge,
            color = colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        trailingView?.let { trailingView() }
    }
}

@Composable
fun NoChatsWelcomeNotice(
    modifier: Modifier = Modifier,
    onStartChatClicked: () -> Unit,
) {
    EmptyContentNotice(
        modifier = modifier,
        leadingView = {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(72.dp),
                imageVector = Icons.Rounded.Key,
                contentDescription = null,
                tint = colorScheme.onSurface,
            )
        },
        title = stringResource(R.string.welcome_to_keys_ai),
        details = stringResource(R.string.no_chats_details),
        trailingView = {
            Spacer(Modifier.height(spacing.small))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onStartChatClicked,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.start_first_chat_button)
                )
                Spacer(modifier = Modifier.size(spacing.default))
                Text(text = stringResource(R.string.start_first_chat_button))
            }
        }
    )
}

@Composable
fun NoKeysWelcomeNotice(
    modifier: Modifier = Modifier,
    onAddKeyClicked: () -> Unit,
) {
    EmptyContentNotice(
        modifier = modifier,
        leadingView = {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(72.dp),
                imageVector = Icons.Rounded.Key,
                contentDescription = null,
                tint = colorScheme.onSurface,
            )
        },
        title = stringResource(R.string.welcome_to_keys_ai),
        details = stringResource(R.string.no_keys_details),
        trailingView = {
            Spacer(Modifier.height(spacing.small))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onAddKeyClicked,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_first_key_button)
                )
                Spacer(modifier = Modifier.size(spacing.default))
                Text(text = stringResource(R.string.add_first_key_button))
            }
        }
    )
}

@PreviewLightDark
@Composable
fun NoChatsPreview() {
    AndroidaiTheme {
        NoChatsWelcomeNotice(
            modifier = Modifier.background(colorScheme.surface),
            onStartChatClicked = {},
        )
    }
}

@PreviewLightDark
@Composable
fun NoKeysPreview() {
    AndroidaiTheme {
        NoKeysWelcomeNotice(
            modifier = Modifier.background(colorScheme.surface),
            onAddKeyClicked = {},
        )
    }
}