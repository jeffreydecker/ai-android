package com.itsdecker.androidai.screens.shared.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import com.itsdecker.androidai.R
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.database.ConversationEntity
import com.itsdecker.androidai.database.ConversationWithMessages
import com.itsdecker.androidai.screens.apikeyslist.ApiKeysList
import com.itsdecker.androidai.screens.shared.dialogs.DeleteConfirmationDialog
import com.itsdecker.androidai.screens.shared.dialogs.RenameDialog
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.cornerRadius

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationSettingsBottomSheet(
    conversation: ConversationEntity?,
    apiKeys: List<ApiKeyEntity>,
    defaultApiKeyId: String?,
    selectedApiKey: ApiKeyEntity?,
    showBottomSheet: Boolean,
    onApiKeyClicked: (apiKey: ApiKeyEntity) -> Unit,
    onEditApiKeyClicked: (apiKey: ApiKeyEntity) -> Unit,
    onSaveChatName: (chatName: String) -> Unit,
    onDeleteChatConfirmed: () -> Unit,
    onHideBottomSheet: () -> Unit,
) {
    val showRenameChatDialog = remember { mutableStateOf(false) }
    val showKeySelectionDialog = remember { mutableStateOf(false) }
    val showDeleteChatDialog = remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onHideBottomSheet, sheetState = sheetState
        ) {
            ConversationSettingsList(
                onRenameClicked = {
                    onHideBottomSheet()
                    showRenameChatDialog.value = true
                },
                onChangeKeyClicked = {
                    onHideBottomSheet()
                    showKeySelectionDialog.value = true
                },
                onChangeModelClicked = {}, // TODO
                onDeleteClicked = {
                    onHideBottomSheet()
                    showDeleteChatDialog.value = true
                },
            )
        }
    }

    if (showRenameChatDialog.value) {
        conversation?.title?.let {
            Dialog(
                onDismissRequest = { showRenameChatDialog.value = false },
            ) {
                RenameDialog(initialInputText = conversation.title,
                    onSaveConfirmed = { newChatName ->
                        onSaveChatName(newChatName)
                        showRenameChatDialog.value = false
                    },
                    onDismiss = { showRenameChatDialog.value = false })
            }
        }
    }

    if (showKeySelectionDialog.value) {
        Dialog(
            onDismissRequest = { showKeySelectionDialog.value = false },
        ) {
            ApiKeysList(
                apiKeys = apiKeys,
                defaultKeyId = defaultApiKeyId,
                selectedKeyId = selectedApiKey?.id,
                onItemClick = { apiKeyId ->
                    apiKeys.firstOrNull { it.id == apiKeyId }?.let { apiKey ->
                        onApiKeyClicked(apiKey)
                        showKeySelectionDialog.value = false
                    }
                },
                onItemLongClick = { apiKeyId ->
                    apiKeys.firstOrNull { it.id == apiKeyId }?.let { apiKey ->
                        onEditApiKeyClicked(apiKey)
                        showKeySelectionDialog.value = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(cornerRadius.large))
                    .background(color = colorScheme.surfaceContainer),
            )
        }
    }

    if (showDeleteChatDialog.value) {
        conversation?.title?.let {
            Dialog(
                onDismissRequest = { showDeleteChatDialog.value = false },
            ) {
                DeleteConfirmationDialog(deletionTargetText = "\"${conversation.title}\"",
                    onDeleteConfirmed = {
                        onDeleteChatConfirmed()
                        showDeleteChatDialog.value = false
                    },
                    onDismiss = { showDeleteChatDialog.value = false })
            }
        }
    }
}

@Composable
fun ConversationSettingsList(
    onRenameClicked: () -> Unit,
    onChangeKeyClicked: () -> Unit,
    onChangeModelClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    Column {
        ConversationItem(
            title = stringResource(R.string.edit_chat_name),
            icon = Icons.Rounded.Edit,
            isDestructive = false,
            onClick = onRenameClicked,
        )
        HorizontalDivider()
        ConversationItem(
            title = stringResource(R.string.change_chat_key),
            icon = Icons.Rounded.Key,
            isDestructive = false,
            onClick = onChangeKeyClicked,
        )
//        ConversationItem(
//            title = stringResource(R.string.change_chat_key_model),
//            icon = Icons.Rounded.Tune,
//            isDestructive = false,
//            onClick = onChangeModelClicked,
//        )
        HorizontalDivider()
        ConversationItem(
            title = stringResource(R.string.delete_chat),
            icon = Icons.Rounded.Delete,
            isDestructive = true,
            onClick = onDeleteClicked,
        )
    }
}

@Composable
fun ConversationItem(
    title: String,
    icon: ImageVector,
    isDestructive: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when (isDestructive) {
        true -> colorScheme.error
        false -> colorScheme.onSurface
    }

    val containerColor = ListItemDefaults.colors(
        containerColor = Color.Transparent,
    )

    ListItem(
        headlineContent = {
            Text(
                text = title,
                color = contentColor
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
            )
        },
        colors = containerColor,
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@PreviewLightDark
@Composable
fun PreviewConversationBottomSheet() {
    AndroidaiTheme {
        Box(
            modifier = Modifier.background(colorScheme.surfaceContainer)
        ) {
            ConversationSettingsList(
                onRenameClicked = {},
                onChangeKeyClicked = {},
                onChangeModelClicked = {},
                onDeleteClicked = {},
            )
        }
    }
}