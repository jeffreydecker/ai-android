package com.itsdecker.androidai.screens.conversations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.itsdecker.androidai.R
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationWithApiKey
import com.itsdecker.androidai.screens.preview.apiKeyPreviewList
import com.itsdecker.androidai.screens.preview.conversationsList
import com.itsdecker.androidai.screens.shared.NoChatsWelcomeNotice
import com.itsdecker.androidai.screens.shared.NoKeysWelcomeNotice
import com.itsdecker.androidai.screens.shared.ScreenHeader
import com.itsdecker.androidai.screens.shared.ScrollableContainer
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing
import com.itsdecker.androidai.utils.TimeAgoFormatter

@Composable
fun ConversationsScreen(
    modifier: Modifier = Modifier,
    viewModel: ConversationsViewModel,
) {
    val conversations = viewModel.conversations.collectAsState()
    val apiKeys = viewModel.apiKeys.collectAsState()

    ConversationsScreen(
        modifier = modifier,
        conversations = conversations.value,
        apiKeys = apiKeys.value,
        onConversationClicked = viewModel::goToConversation,
        onNewChatClicked = viewModel::startNewConversation,
        onAddKeyClicked = viewModel::goToAddKey,
        onKeysClicked = viewModel::goToKeys,
        onCloseClicked = viewModel::goBack,
    )
}

@Composable
fun ConversationsScreen(
    modifier: Modifier = Modifier,
    conversations: List<ConversationWithApiKey>,
    apiKeys: List<ApiKeyEntity>,
    onConversationClicked: (conversationId: String, apiKeyId: String) -> Unit,
    onNewChatClicked: () -> Unit,
    onAddKeyClicked: () -> Unit,
    onKeysClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    AndroidaiTheme {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            ScreenHeader(
                title = stringResource(R.string.your_chats_title),
                subtitle = null,
                leadingIcon = {
                    IconButton(
                        onClick = onCloseClicked,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.close_option),
                            tint = colorScheme.onSurface,
                        )
                    }
                },
                trailingActions = {
                    IconButton(
                        onClick = onKeysClicked,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Key,
                            tint = colorScheme.onSurface,
                            contentDescription = stringResource(R.string.your_keys_button),
                        )
                    }
                }
            )

            val contentModifier = Modifier
                .background(color = colorScheme.surface)
                .navigationBarsPadding()
                .weight(1f)

            if (conversations.isEmpty()) {
                Box(
                    modifier = contentModifier.padding(spacing.large),
                ) {
                    val emptyContentModifier = Modifier.align(Alignment.Center)

                    if (apiKeys.isEmpty()) {
                        NoKeysWelcomeNotice(
                            modifier = emptyContentModifier,
                            onAddKeyClicked = onAddKeyClicked,
                        )
                    } else {
                        NoChatsWelcomeNotice(
                            modifier = emptyContentModifier,
                            onStartChatClicked = onNewChatClicked,
                        )
                    }
                }

            } else {
                ChatsList(
                    modifier = contentModifier,
                    conversations = conversations,
                    onConversationClicked = onConversationClicked,
                    onNewChatClicked = onNewChatClicked,
                )
            }
        }
    }
}

@Composable
fun ChatsList(
    modifier: Modifier = Modifier,
    conversations: List<ConversationWithApiKey>,
    onConversationClicked: (conversationId: String, apiKeyId: String) -> Unit,
    onNewChatClicked: () -> Unit,
) {
    ScrollableContainer(
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            items(conversations) { conversation ->
                ConversationItem(
                    conversation = conversation,
                    onClick = onConversationClicked,
                )
            }
        }

        NewConversationButton(
            modifier = Modifier
                .padding(all = spacing.medium)
                .align(alignment = Alignment.BottomEnd),
            onClick = onNewChatClicked,
        )
    }
}

@Composable
fun ConversationItem(
    conversation: ConversationWithApiKey,
    onClick: (conversationId: String, apiKeyId: String) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = conversation.conversation.title
                    ?: stringResource(R.string.new_chat_title),
            )
        },
        supportingContent = {
            Text(
                text = TimeAgoFormatter.format(
                    epochMillis = conversation.conversation.updatedAt,
                    context = LocalContext.current,
                )
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(conversation.apiKey.chatModel.icon),
                contentDescription = null,
                tint = conversation.apiKey.chatModel.brandColor,
            )
        },
        modifier = Modifier.clickable {
            onClick(
                conversation.conversation.id,
                conversation.apiKey.id,
            )
        }
    )
}

@Composable
fun NewConversationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = stringResource(R.string.new_chat_button))
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.new_chat_button),
            )
        },
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
fun ConversationsScreenPreview() {
    AndroidaiTheme {
        ConversationsScreen(
            conversations = conversationsList(),
            apiKeys = apiKeyPreviewList(),
            onConversationClicked = { _, _ -> },
            onNewChatClicked = {},
            onAddKeyClicked = {},
            onKeysClicked = {},
            onCloseClicked = {},
        )
    }
}

@PreviewLightDark
@Composable
fun NoChatsConversationsScreenPreview() {
    AndroidaiTheme {
        ConversationsScreen(
            conversations = emptyList(),
            apiKeys = apiKeyPreviewList(),
            onConversationClicked = { _, _ -> },
            onNewChatClicked = {},
            onAddKeyClicked = {},
            onKeysClicked = {},
            onCloseClicked = {},
        )
    }
}

@PreviewLightDark
@Composable
fun NoKeysConversationsScreenPreview() {
    AndroidaiTheme {
        ConversationsScreen(
            conversations = emptyList(),
            apiKeys = emptyList(),
            onConversationClicked = { _, _ -> },
            onNewChatClicked = {},
            onAddKeyClicked = {},
            onKeysClicked = {},
            onCloseClicked = {},
        )
    }
}