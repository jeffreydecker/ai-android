package com.itsdecker.androidai.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.requests.claude.ANTHROPIC_MESSENGER_ROLE_ASSISTANT
import com.itsdecker.androidai.requests.claude.ANTHROPIC_MESSENGER_ROLE_USER
import com.itsdecker.androidai.requests.claude.ClaudeApiError
import com.itsdecker.androidai.ui.theme.spacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// Composable UI updated to show conversation history
@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val conversation by viewModel.conversation.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    ChatWindow(
        conversation,
        isLoading,
        error,
    ) { viewModel.sendMessage(it) }
}

@Composable
fun ChatWindow(
    conversation: Conversation,
    isLoading: Boolean,
    error: ClaudeApiError?,
    sendMessage: (String) -> Unit,
) {
    // TODO - This is currently and easy way to keep chat scrolling. Longer term it would be nice
    //  to modify this to scroll to show the last the beginning of the latest response if it doesn't
    //  fit within the chat window. This is pretty easy to do with a naive approach but might be fun
    //  to tackle as a more complex task.
    val chatListState = rememberLazyListState()
    LaunchedEffect(conversation.messages) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        // Chat list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = chatListState,
        ) {
            itemsIndexed(conversation.messages) { _, message ->
                ChatBubble(message)
            }
        }

        error?.let { errorMessage -> ErrorMessage(errorMessage) }

        ChatInput(isLoading, sendMessage)
    }
}

@Composable
private fun ChatInput(
    isLoading: Boolean,
    sendMessage: (String) -> Unit,
) {
    var prompt by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(MaterialTheme.spacing.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier.weight(1f)
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
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.role == ANTHROPIC_MESSENGER_ROLE_USER
    val backgroundColor = if (isUser)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.secondaryContainer

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.extraSmall),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(Date(message.timestamp)),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
        )
    }
}

@Composable
fun ErrorMessage(error: ClaudeApiError) {
    val (icon, message) = when (error) {
        is ClaudeApiError.AuthenticationError -> Icons.Rounded.Lock to error.message
        is ClaudeApiError.RateLimitError -> Icons.Rounded.Timer to error.message
        is ClaudeApiError.OverloadedError -> Icons.Rounded.CloudOff to error.message
        is ClaudeApiError.NetworkError -> Icons.Rounded.WifiOff to error.message
        is ClaudeApiError.RequestTooLarge -> Icons.Rounded.ContentCopy to error.message
        else -> Icons.Rounded.Error to (error.message ?: "")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
            .background(
                MaterialTheme.colorScheme.errorContainer,
                RoundedCornerShape(MaterialTheme.spacing.small)
            )
            .padding(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    ChatWindow(
        conversation = Conversation(
            messages = listOf(
                ChatMessage(ANTHROPIC_MESSENGER_ROLE_USER, "what did I say?"),
                ChatMessage(ANTHROPIC_MESSENGER_ROLE_ASSISTANT, "this is the start of our conversation... you said nothing"),
            )
        ),
        isLoading = false,
        error = ClaudeApiError.InvalidRequest(message = "Shit went down"),
        sendMessage = {},
    )
}