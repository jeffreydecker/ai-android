package com.itsdecker.androidai.screens.apikeyslist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsdecker.androidai.R
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.screens.preview.ThemePreviews
import com.itsdecker.androidai.screens.preview.apiKeyPreviewList
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun ApiKeysListScreen(
    modifier: Modifier = Modifier,
    viewModel: ApiKeysListViewModel,
) {
    val apiKeys by viewModel.apiKeys.collectAsState()
    val defaultKeyId by viewModel.defaultApiKeyId.collectAsState()

    ApiKeysListScreen(
        modifier = modifier,
        apiKeys = apiKeys,
        defaultKeyId = defaultKeyId,
        onApiKeyClicked = viewModel::goToEditKey,
        onApiKeyLongClicked = {},
        onAddApiKeyClicked = viewModel::goToAddKey,
    )
}

@Composable
fun ApiKeysListScreen(
    modifier: Modifier = Modifier,
    apiKeys: List<ApiKeyEntity>,
    defaultKeyId: String? = null,
    selectedKeyId: String? = null,
    onApiKeyClicked: (apiKeyId: String) -> Unit,
    onApiKeyLongClicked: (apiKeyId: String) -> Unit,
    onAddApiKeyClicked: () -> Unit,
) {
    AndroidaiTheme {
        Box(modifier = modifier.fillMaxSize()) {
            ApiKeysList(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorScheme.surface),
                apiKeys = apiKeys,
                defaultKeyId = defaultKeyId,
                selectedKeyId = selectedKeyId,
                onItemClick = onApiKeyClicked,
                onItemLongClick = onApiKeyLongClicked,
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
    modifier: Modifier = Modifier,
    apiKeys: List<ApiKeyEntity>,
    defaultKeyId: String? = null,
    selectedKeyId: String? = null,
    onItemClick: (apiKeyId: String) -> Unit,
    onItemLongClick: (apiKeyId: String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = apiKeys) { apiKey ->
            ApiKeyItem(
                apiKey = apiKey,
                isDefault = apiKey.id == defaultKeyId,
                isSelected = apiKey.id == selectedKeyId,
                onClick = onItemClick,
                onLongClick = onItemLongClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ApiKeyItem(
    apiKey: ApiKeyEntity,
    isDefault: Boolean,
    isSelected: Boolean,
    onClick: (apiKeyId: String) -> Unit,
    onLongClick: (apiKeyId: String) -> Unit,
) {
    ListItem(
        overlineContent = {
            if (isDefault) {
                Text(text = "Default")
            }
        },
        headlineContent = { Text(text = apiKey.name) },
        supportingContent = { Text(text = apiKey.description) },
        leadingContent = {
            Icon(
                painter = painterResource(apiKey.chatModel.icon),
                contentDescription = null,
                tint = apiKey.chatModel.brandColor,
            )
        },
        trailingContent = {
            if (isDefault) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                )
            }
        },
        colors = when (isSelected) {
            true -> getSelectedItemColors()
            false -> getItemColors()
        },
        modifier = Modifier.combinedClickable(
            onClick = { onClick(apiKey.id) },
            onLongClick = { onLongClick(apiKey.id) },
        )
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

@Composable
private fun getSelectedItemColors(): ListItemColors =
    ListItemDefaults.colors(
        containerColor = colorScheme.surfaceContainerLow,
    )

@Composable
private fun getItemColors(): ListItemColors =
    ListItemDefaults.colors(
        containerColor = Color.Transparent,
    )

@ThemePreviews
@Composable
private fun MainScreenPreview() {
    ApiKeysListScreen(
        apiKeys = apiKeyPreviewList(),
        defaultKeyId = "2",
        selectedKeyId = "1",
        onApiKeyClicked = {},
        onApiKeyLongClicked = {},
        onAddApiKeyClicked = {},
    )
}