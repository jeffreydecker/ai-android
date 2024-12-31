package com.itsdecker.androidai.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Comment
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.R
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.ChatApiError
import com.itsdecker.androidai.network.anthropic.ANTHROPIC_MESSENGER_ROLE_USER
import com.itsdecker.androidai.screens.apikeyslist.ApiKeysList
import com.itsdecker.androidai.screens.apikeyslist.ApiKeysViewModel
import com.itsdecker.androidai.screens.preview.ThemePreviews
import com.itsdecker.androidai.screens.preview.apiKeyPreviewList
import com.itsdecker.androidai.screens.preview.chatMessagesPreviewList
import com.itsdecker.androidai.screens.shared.ScrollableContainer
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.cornerRadius
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    apiKeysViewModel: ApiKeysViewModel,
) {
    val conversation by viewModel.conversation.collectAsState()
    val apiKeys by apiKeysViewModel.apiKeys.collectAsState()
    val defaultApiKeyId by apiKeysViewModel.defaultApiKeyId.collectAsState()
    val selectedApiKey by viewModel.selectedApiKey.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    ChatWindow(
        conversation = conversation,
        apiKeys = apiKeys,
        defaultApiKeyId = defaultApiKeyId,
        selectedApiKey = selectedApiKey,
        isLoading = isLoading,
        error = error,
        onSendMessage = viewModel::sendMessage,
        onChatsClicked = viewModel::goToConversations,
        onApiKeyClicked = viewModel::updateSelectedApiKey,
        onAddApiKeyClicked = viewModel::goToAddApiKey,
        onEditApiKeyClicked = { apiKeyEntity -> apiKeysViewModel.goToEditKey(apiKeyEntity.id) },
        onApiKeySettingsClicked = viewModel::goToApiKeySettings,
    )
}

@Composable
fun ChatWindow(
    conversation: ConversationWithMessages?,
    apiKeys: List<ApiKeyEntity>,
    defaultApiKeyId: String?,
    selectedApiKey: ApiKeyEntity?,
    isLoading: Boolean,
    error: ChatApiError?,
    onSendMessage: (message: String) -> Unit,
    onChatsClicked: () -> Unit,
    onApiKeyClicked: (apiKey: ApiKeyEntity) -> Unit,
    onAddApiKeyClicked: () -> Unit,
    onEditApiKeyClicked: (apiKey: ApiKeyEntity) -> Unit,
    onApiKeySettingsClicked: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorScheme.surface),
        ) {
            ChatHeader(
                chatTitle = conversation?.conversation?.title,
                apiKeyName = selectedApiKey?.name,
                onChatsClicked = onChatsClicked,
                onKeysClicked = onApiKeySettingsClicked,
            )

            ChatContent(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                conversation = conversation,
                apiKeys = apiKeys,
                defaultApiKeyId = defaultApiKeyId,
                selectedApiKey = selectedApiKey,
                onApiKeyClicked = onApiKeyClicked,
                onAddApiKeyClicked = onAddApiKeyClicked,
                onEditApiKeyClicked = onEditApiKeyClicked,
                onApiKeySettingsClicked = onApiKeySettingsClicked,
            )

            error?.let { errorMessage -> ErrorMessage(errorMessage) }

            ChatInput(isLoading, onSendMessage)
        }
    }
}

@Composable
fun ChatHeader(
    chatTitle: String?,
    apiKeyName: String?,
    onChatsClicked: () -> Unit,
    onKeysClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(color = colorScheme.surface)
            .padding(all = spacing.small),
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
    ) {
        IconButton(
            onClick = onChatsClicked,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Comment,
                tint = colorScheme.onSurface,
                contentDescription = stringResource(R.string.my_chats_button),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = spacing.small)
                .weight(weight = 1f),
        ) {
            Text(
                text = chatTitle ?: stringResource(R.string.new_chat_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            apiKeyName?.let {
                Text(
                    text = apiKeyName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        IconButton(
            onClick = onKeysClicked,
        ) {
            Icon(
                imageVector = Icons.Rounded.Key,
                tint = colorScheme.onSurface,
                contentDescription = stringResource(R.string.my_keys_button),
            )
        }

        // TODO - For adjustment and maybe changing provider?
//        IconButton(
//            onClick = onKeysClicked,
//        ) {
//            Icon(
//                imageVector = Icons.Rounded.Settings,
//                tint = colorScheme.onSurface,
//                contentDescription = stringResource(R.string.my_keys_button),
//            )
//        }
    }
}

@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    conversation: ConversationWithMessages?,
    apiKeys: List<ApiKeyEntity>,
    defaultApiKeyId: String?,
    selectedApiKey: ApiKeyEntity?,
    onApiKeyClicked: (apiKey: ApiKeyEntity) -> Unit,
    onAddApiKeyClicked: () -> Unit,
    onEditApiKeyClicked: (apiKey: ApiKeyEntity) -> Unit,
    onApiKeySettingsClicked: () -> Unit,
) {
    // TODO - This is currently and easy way to keep chat scrolling. Longer term it would be nice
    //  to modify this to scroll to show the last the beginning of the latest response if it doesn't
    //  fit within the chat window. This is pretty easy to do with a naive approach but might be fun
    //  to tackle as a more complex task.
    val chatListState = rememberLazyListState()
    LaunchedEffect(conversation?.messages) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }

    ScrollableContainer(
        modifier = modifier,
    ) {
        if (conversation?.messages?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(all = spacing.medium),
                state = chatListState,
                verticalArrangement = Arrangement.spacedBy(spacing.small)
            ) {
                items(conversation.messages) { message ->
                    ChatBubble(message)
                }
            }
        } else {
            EmptyChatPlaceholder(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(spacing.medium)
                    .fillMaxHeight(0.5f),
                apiKeys = apiKeys,
                defaultApiKeyId = defaultApiKeyId,
                selectedApiKey = selectedApiKey,
                onApiKeyClicked = onApiKeyClicked,
                onAddApiKeyClicked = onAddApiKeyClicked,
                onApiKeyLongClicked = onEditApiKeyClicked,
                onApiKeySettingsClicked = onApiKeySettingsClicked,
            )
        }
    }
}

@Composable
private fun EmptyChatPlaceholder(
    modifier: Modifier = Modifier,
    apiKeys: List<ApiKeyEntity>,
    defaultApiKeyId: String?,
    selectedApiKey: ApiKeyEntity?,
    onApiKeyClicked: (apiKey: ApiKeyEntity) -> Unit,
    onApiKeyLongClicked: (apiKey: ApiKeyEntity) -> Unit,
    onAddApiKeyClicked: () -> Unit,
    onApiKeySettingsClicked: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            Row {
                Text(
                    text = "Your Api Keys",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(horizontal = spacing.medium)
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                )

                IconButton(
                    onClick = onApiKeySettingsClicked,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Key,
                        tint = colorScheme.onSurface,
                        contentDescription = stringResource(R.string.my_keys_button),
                    )
                }
            }

            // TODO - If no keys show a getting started message
            ApiKeysList(
                apiKeys = apiKeys,
                defaultKeyId = defaultApiKeyId,
                selectedKeyId = selectedApiKey?.id,
                onItemClick = { apiKeyId ->
                    apiKeys.firstOrNull { it.id == apiKeyId }
                        ?.let { apiKey -> onApiKeyClicked(apiKey) }
                },
                onItemLongClick = { apiKeyId ->
                    apiKeys.firstOrNull { it.id == apiKeyId }
                        ?.let { apiKey -> onApiKeyLongClicked(apiKey) }
                },
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(cornerRadius.large))
                    .background(color = colorScheme.surfaceContainer)
                    .fillMaxHeight()
            )
        }

        SmallFloatingActionButton(
            content = {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_key_button),
                )
            },
            onClick = onAddApiKeyClicked,
            shape = CircleShape,
            modifier = Modifier
                .padding(all = spacing.small)
                .align(Alignment.BottomEnd),
        )
    }
}

@Composable
private fun ChatInput(
    isLoading: Boolean,
    sendMessage: (String) -> Unit,
) {
    var prompt by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .background(
                color = colorScheme.surfaceContainer,
                shape = RoundedCornerShape(
                    topStart = cornerRadius.large,
                    topEnd = cornerRadius.large,
                )
            )
            .navigationBarsPadding()
            .padding(spacing.small),
        horizontalArrangement = Arrangement.spacedBy(spacing.small),
    ) {
        TextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            ),
            placeholder = { Text("Start Chatting") }
        )

        IconButton(
            onClick = {
                sendMessage(prompt)
                prompt = ""
            },
            enabled = !isLoading,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                tint = colorScheme.onSurface,
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun ChatBubble(message: MessageEntity) {
    val isUser = message.role == ANTHROPIC_MESSENGER_ROLE_USER
    val (backgroundColor, itemPadding) = when (isUser) {
        true -> colorScheme.surfaceContainerHigh to spacing.small
        false -> colorScheme.surface to spacing.none
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.extraSmall),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (isUser) {
            Text(
                text = stringResource(R.string.user_label),
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = spacing.small)
            )
        }

        BoxWithConstraints {
            Surface(
                color = backgroundColor,
                shape = RoundedCornerShape(itemPadding),
                modifier = Modifier.widthIn(
                    min = 0.dp,
                    max = if (isUser) maxWidth * 0.7f else maxWidth,
                )
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(itemPadding),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ErrorMessage(error: ChatApiError) {
    val (icon, message) = when (error) {
        is ChatApiError.AuthenticationError -> Icons.Rounded.Lock to error.message
        is ChatApiError.RateLimitError -> Icons.Rounded.Timer to error.message
        is ChatApiError.OverloadedError -> Icons.Rounded.CloudOff to error.message
        is ChatApiError.NetworkError -> Icons.Rounded.WifiOff to error.message
        is ChatApiError.RequestTooLarge -> Icons.Rounded.ContentCopy to error.message
        else -> Icons.Rounded.Error to (error.message ?: "")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing.small)
            .background(
                colorScheme.errorContainer,
                RoundedCornerShape(spacing.small)
            )
            .padding(spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorScheme.error
        )

        Spacer(modifier = Modifier.width(spacing.small))

        Text(
            text = message,
            color = colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@ThemePreviews
@Composable
fun ScreenPreviewWithChat() {
    AndroidaiTheme {
        ChatWindow(
            conversation = chatMessagesPreviewList(),
            apiKeys = emptyList(),
            defaultApiKeyId = null,
            selectedApiKey = null,
            isLoading = false,
            error = ChatApiError.InvalidRequest(message = "Shit went down"),
            onSendMessage = {},
            onChatsClicked = {},
            onApiKeyClicked = {},
            onAddApiKeyClicked = {},
            onEditApiKeyClicked = {},
            onApiKeySettingsClicked = {},
        )
    }
}

@ThemePreviews
@Composable
fun ScreenPreviewWithKeys() {
    AndroidaiTheme {
        ChatWindow(
            conversation = null,
            apiKeys = apiKeyPreviewList(),
            defaultApiKeyId = apiKeyPreviewList()[1].id,
            selectedApiKey = apiKeyPreviewList().first(),
            isLoading = false,
            error = null,
            onSendMessage = {},
            onChatsClicked = {},
            onApiKeyClicked = {},
            onAddApiKeyClicked = {},
            onEditApiKeyClicked = {},
            onApiKeySettingsClicked = {},
        )
    }
}