package com.itsdecker.androidai.screens.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.itsdecker.androidai.R
import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val apiKeys by viewModel.chatModels.collectAsState()

    MainWindow(
        apiKeys = apiKeys,
        onApiKeyClicked = viewModel::goToChat,
        onAddApiKeyClicked = viewModel::goToAddModel,
    )
}

@Composable
private fun MainWindow(
    apiKeys: List<ApiKeyEntity>,
    onApiKeyClicked: (apiKeyId: String) -> Unit,
    onAddApiKeyClicked: () -> Unit,
) {
    AndroidaiTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ApiKeysList(
                apiKeys = apiKeys,
                onItemClick = onApiKeyClicked,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorScheme.surface),
            )

            AddApiKeyButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(spacing.medium),
                onClick = onAddApiKeyClicked,
            )
        }
    }
}

@Composable
fun ApiKeysList(
    apiKeys: List<ApiKeyEntity>,
    onItemClick: (apiKeyId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = apiKeys) { apiKey ->
            ApiKeyItem(
                apiKey = apiKey,
                onClick = onItemClick,
            )
        }
    }
}

@Composable
fun ApiKeyItem(
    apiKey: ApiKeyEntity,
    onClick: (apiKeyId: String) -> Unit,
) {
    ListItem(
        headlineContent = { Text(text = apiKey.name) },
        supportingContent = { Text(text = apiKey.description) },
        leadingContent = {
            Icon(
                painter = painterResource(apiKey.chatModel.icon),
                contentDescription = null,
                tint = apiKey.chatModel.brandColor,
            )
        },
        modifier = Modifier.clickable { onClick(apiKey.id) }
    )
}

@Composable
fun AddApiKeyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = stringResource(R.string.add_key_button))
       },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.add_key_button),

            )
        },
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun ModelItemPreview() {
    ApiKeyItem(
        apiKey = getChatModelPreviewList().first(),
        onClick = {},
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun MainScreenPreview() {
    MainWindow(
        getChatModelPreviewList(),
        {},
        {},
    )
}

private fun getChatModelPreviewList(): List<ApiKeyEntity> = listOf(
    ApiKeyEntity(
        id = "",
        createdAt = System.currentTimeMillis(),
        name = "My Anthropic Key",
        description = "This is my general purpose Anthropic key",
        apiKey = "",
        chatModel = SupportedProvider.Anthropic,
    )
)