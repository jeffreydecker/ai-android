package com.itsdecker.androidai.screens.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.R
import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.screens.shared.IconListItem
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier,
) {
    val chatModels by mainViewModel.chatModels.collectAsState()

    MainWindow(
        chatModels,
        mainViewModel::goToChat,
        mainViewModel::goToAddModel,
        mainViewModel::clearTable,
        modifier,
    )
}

@Composable
private fun MainWindow(
    chatModels: List<ApiKeyEntity>,
    goToChat: (apiKeyId: String) -> Unit,
    goToAddModel: () -> Unit,
    clearTable: () -> Unit,
    modifier: Modifier,
) {
    AndroidaiTheme {
        Box(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.5f)
                    .padding(bottom = MaterialTheme.spacing.medium)
                    .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default, Alignment.Bottom),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.app_title),
                    style = Typography.displayMedium
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.app_prompt),
                    style = Typography.bodySmall
                )
            }

            ApiKeysList(
                chatModels = chatModels,
                onItemClick = goToChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.5f)
                    .padding(
                        start = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.large,
                        bottom = MaterialTheme.spacing.large,
                    )
                    .align(Alignment.BottomCenter),
            )

            AddApiKeyButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = goToAddModel,
            )

            DeleteKeysButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                onClick = clearTable,
            )
        }
    }
}

@Composable
fun ApiKeysList(
    chatModels: List<ApiKeyEntity>,
    onItemClick: (apiKeyId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
        LazyColumn(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceDim,
                    shape = RoundedCornerShape(MaterialTheme.spacing.default),
                ),
        ) {
            items(items = chatModels) { chatModel ->
                ApiKeyItem(
                    chatModel = chatModel,
                    onClick = onItemClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.default),
                )
            }
        }
}

@Composable
fun ApiKeyItem(
    chatModel: ApiKeyEntity,
    onClick: (apiKeyId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconListItem(
        icon = painterResource(chatModel.chatModel.icon),
        iconTint = chatModel.chatModel.brandColor,
        title = chatModel.name,
        subTitle = chatModel.description,
        onClick = { onClick(chatModel.id) },
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
fun DeleteKeysButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        text = {
            Text(text ="Delete Api Keys")
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null,

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
        chatModel = getChatModelPreviewList().first(),
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.default),
    )
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MainScreenPreview() {
    MainWindow(
        getChatModelPreviewList(),
        {},
        {},
        {},
        Modifier.fillMaxSize(),
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