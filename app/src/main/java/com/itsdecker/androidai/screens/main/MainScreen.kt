package com.itsdecker.androidai.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.data.SupportedModel
import com.itsdecker.androidai.database.ChatModelEntity
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.Typography

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
) {
    val chatModels by mainViewModel.chatModels.collectAsState()

    MainWindow(
        chatModels,
        { mainViewModel.goToChat() },
        { mainViewModel.goToAddModel() },
    )
}

@Composable
private fun MainWindow(
    chatModels: List<ChatModelEntity>,
    goToChat: () -> Unit,
    goToAddModel: () -> Unit,
) {
    AndroidaiTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Android.AI",
                    style = Typography.displayMedium
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Add or select a model to get started",
                    style = Typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = goToChat,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.7f)
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(
                        text = "Chat Now",
                    )
                }
                Text(
                    text = "${chatModels.count()}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                // TODO - Add Models List Here
            }

            AddModelInfoButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = goToAddModel,
            )
        }
    }
}

@Composable
fun AddModelInfoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        text = {
            Text("Add a Model")
       },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add Model"

            )
        },
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainWindow(
        listOf(
            ChatModelEntity(
                id = "",
                createdAt = System.currentTimeMillis(),
                name = "My Claude Model",
                description = "",
                apiKey = "",
                chatModel = SupportedModel.CLAUDE,
            )
        ),
        {},
        {},
    )
}