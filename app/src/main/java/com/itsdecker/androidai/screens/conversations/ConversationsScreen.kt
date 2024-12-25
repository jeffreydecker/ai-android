package com.itsdecker.androidai.screens.conversations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.itsdecker.androidai.screens.chat.Conversation
import com.itsdecker.androidai.screens.shared.TitleSubtitleItem

@Composable
fun ConversationsScreen(viewModel: ConversationsViewModel) {
//    ConversationsView()
}

@Composable
fun ConversationsView(
    conversations: List<Conversation>,
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("The Model")
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(conversations) { conversation ->
                TitleSubtitleItem(
                    title = conversation.id
                )
            }
        }
    }
}

@Composable
fun ConversationItem() {

}