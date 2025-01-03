package com.itsdecker.androidai.screens.chat

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.R
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.database.MessageEntity
import com.itsdecker.androidai.network.ChatRole
import com.itsdecker.androidai.screens.shared.components.LoadingDots
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Deprecated("I think reversed is probably the right way to go")
@Composable
fun ChatMessagesView(
    conversation: ConversationWithMessages,
    isLoading: Boolean,
) {
    val listState = rememberLazyListState()
    val assistantResponseOffset = assistantResponseOffset()
    var messageHeight by remember { mutableIntStateOf(0) }
    var isInitialLoad by remember { mutableStateOf(true) }

    LaunchedEffect(conversation.messages.size) {
        // - If this is the initial load then scroll to the bottom
        // - If the last message is a user message then scroll to bottom
        // - If the last message is from the assistant scroll it's top to the top of the screen with
        //   an offset. The offset should help reveal some of the preceding user message.
        if (isInitialLoad && conversation.messages.isNotEmpty()) {
            Log.d("Decker Debug", "Initial load")
            // No need to animate
            listState.scrollToItem(conversation.messages.size)
            isInitialLoad = false
        } else {
            val lastMessage = conversation.messages.lastOrNull()
            lastMessage?.let {
                when (lastMessage.role) {
                    ChatRole.User.value -> listState.animateScrollToItem(conversation.messages.size)
                    ChatRole.Assistant.value -> {
                        // TODO - To make things a little more fun we can loop over assistant messages
                        //  to create a typing effect until we reach the desired offset. Maybe we can
                        //  tackle this in the future but there are also other options.
                        listState.animateScrollToItem(
                            index = conversation.messages.size,
                            scrollOffset = assistantResponseOffset,
                        )
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(all = spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.small),
    ) {
        // This helps single out the last message if we want to do something special with it
        items(conversation.messages.dropLast(1)) { message ->
            ChatBubble(message = message)
        }

        // Handle the last message independently for more specialized logic
        // Right now we're just getting the measured height for assistant messages that we could use
        // to help create a typing effect. That isn't fully implemented at the moment.
        item {
            conversation.messages.lastOrNull()?.let { lastMessage ->
                if (lastMessage.role == ChatRole.User.value) {
                    ChatBubble(message = lastMessage)
                } else {
                    ChatBubble(
                        message = lastMessage,
                        modifier = Modifier.onSizeChanged { size -> messageHeight = size.height }
                    )
                }
            }
        }

        // So assistant loading dots
        if (isLoading) {
            item {
                LoadingDots(modifier = Modifier.padding(start = spacing.tiny))
            }
        }
    }
}

@Composable
fun ChatMessagesViewReversed(
    conversation: ConversationWithMessages,
    isLoading: Boolean,
) {
    val listState = rememberLazyListState()
    var messageHeight by remember { mutableIntStateOf(0) }
    var isInitialLoad by remember { mutableStateOf(true) }

    val assistantResponseOffset = assistantResponseOffset()
    val listContentOffset = listContentOffset()

    LaunchedEffect(conversation.messages.size) {
        // - If this is the initial load start at the last message. If it's larger than the view
        //   port then it should start at the top of the window
        // - If the last message is a user message then scroll to bottom
        // - If the last message is from the assistant scroll it's top to the top of the screen with
        //   an offset. The offset should help reveal some of the preceding user message.

        if (isInitialLoad && conversation.messages.isNotEmpty()) {
            // No need to animate
            listState.scrollToItem(
                index = 1,
                scrollOffset = listContentOffset - listState.layoutInfo.viewportSize.height,
            )
            isInitialLoad = false
        } else {
            val lastMessage = conversation.messages.lastOrNull()
            lastMessage?.let {
                when (lastMessage.role) {
                    // Should already be at the end of the view so this is probably redundant
                    ChatRole.User.value -> listState.scrollToItem(0)
                    ChatRole.Assistant.value -> {
                        // TODO - To make things a little more fun we can loop over assistant
                        //  messages to create a typing effect until we reach the desired offset.
                        //  Maybe we can tackle this in the future but there are also other options.
                        listState.scrollToItem(1)
                        listState.animateScrollToItem(
                            index = 1,
                            scrollOffset = assistantResponseOffset - listState.layoutInfo.viewportSize.height,
                        )
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(all = spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.small, Alignment.Bottom),
        reverseLayout = true,
    ) {
        // Show assistant loading dots
        if (isLoading) {
            item {
                LoadingDots(modifier = Modifier.padding(start = spacing.tiny))
            }
        }

        // Handle the last message independently for more specialized logic
        // Right now we're just getting the measured height for assistant messages that we could use
        // to help create a typing effect. That isn't fully implemented at the moment.
        item {
            conversation.messages.lastOrNull()?.let { lastMessage ->
                if (lastMessage.role == ChatRole.User.value) {
                    ChatBubble(message = lastMessage)
                } else {
                    ChatBubble(
                        message = lastMessage,
                        modifier = Modifier.onSizeChanged { size -> messageHeight = size.height }
                    )
                }
            }
        }

        // This helps single out the last message if we want to do something special with it
        items(conversation.messages.dropLast(1).reversed()) { message ->
            ChatBubble(message = message)
        }
    }
}

@Composable
private fun ChatBubble(
    message: MessageEntity,
    modifier: Modifier = Modifier,
) {
    // TODO - An alternative to the typing effect in the list view would be to doing it here
    //  instead. We could type out the message or do some other things like an alpha reveal.
    //  The only thing we would need here is an `animate` argument.
    val isUser = message.role == ChatRole.User.value
    val (backgroundColor, itemPadding) = when (isUser) {
        true -> colorScheme.surfaceContainerHigh to spacing.small
        false -> colorScheme.surface to spacing.none
    }

    Column(
        modifier = modifier.fillMaxWidth(),
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

// Provides the rough height of a single line user message in PX to help
// assistant messages be displayed with some light context of the previous
// user message above.
@Composable
private fun assistantResponseOffset(): Int {
    val listContentPadding = spacing.medium
    val messageListSpacing = spacing.small * 2 // 2x for above and below
    val userTagTextSize = Typography.bodySmall.fontSize
    val userBubbleVerticalPadding = spacing.small * 2 // 2x for above and below
    val messageTextSize = Typography.bodyLarge.fontSize
    return with(LocalDensity.current) {
        val heightInPx = listContentPadding.toPx() +
                messageListSpacing.toPx() +
                userTagTextSize.toPx() +
                userBubbleVerticalPadding.toPx() +
                messageTextSize.toPx()
        heightInPx.toInt()
    }
}

@Composable
private fun listContentOffset(): Int = with(LocalDensity.current) { spacing.medium.toPx().toInt() }