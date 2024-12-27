package com.itsdecker.androidai.screens.conversations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.R
import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationEntity
import com.itsdecker.androidai.database.ConversationWithApiKey
import com.itsdecker.androidai.screens.chat.Conversation
import com.itsdecker.androidai.screens.shared.IconListItem
import com.itsdecker.androidai.screens.shared.TitleSubtitleListItem
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.cornerRadius
import com.itsdecker.androidai.ui.theme.spacing
import com.itsdecker.androidai.utils.TimeAgoFormatter
import java.text.SimpleDateFormat

@Composable
fun ConversationsScreen(
    viewModel: ConversationsViewModel,
) {
    val conversations = viewModel.conversations.collectAsState()
    ConversationsView(
        conversations = conversations.value,
        onConversationClicked = viewModel::goToConversation,
        onNewChatClicked = viewModel::startNewConversation,
    )
}

@Composable
fun ConversationsView(
    conversations: List<ConversationWithApiKey>,
    onConversationClicked: (conversationId: String, apiKeyId: String) -> Unit,
    onNewChatClicked: () -> Unit,
) {
    AndroidaiTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    ?: stringResource(R.string.untitled_conversation),
            )
        },
        supportingContent = {
            Text(
                text = TimeAgoFormatter.format(
                    epochMillis = conversation.conversation.createdAt,
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

@Preview(showBackground = true)
@Composable
fun ConversationsScreenPreview() {
    ConversationsView(
        conversations = listOf(
            ConversationWithApiKey(
                conversation = ConversationEntity(
                    apiKeyId = "fakeApiKey",
                    title = "Conversation 1",
                ),
                apiKey = ApiKeyEntity(
                    name = "Anthropic Key",
                    description = "My Anthropic API key",
                    apiKey = "fakeKey",
                    chatModel = SupportedProvider.Anthropic,
                )
            ),
            ConversationWithApiKey(
                conversation = ConversationEntity(
                    apiKeyId = "fakeApiKey",
                    title = "Conversation 2",
                ),
                apiKey = ApiKeyEntity(
                    name = "OpenAI Key",
                    description = "My OpenAI API key",
                    apiKey = "fakeKey",
                    chatModel = SupportedProvider.OpenAI,
                )
            ),
            ConversationWithApiKey(
                conversation = ConversationEntity(
                    apiKeyId = "fakeApiKey",
                    title = "Conversation 3",
                ),
                apiKey = ApiKeyEntity(
                    name = "Google Key",
                    description = "My Google API key",
                    apiKey = "fakeKey",
                    chatModel = SupportedProvider.Google,
                )
            ),
        ),
        onConversationClicked = { _, _ -> },
        onNewChatClicked = {},
    )
}