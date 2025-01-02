package com.itsdecker.androidai.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.itsdecker.androidai.R
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.colorScheme

@Composable
fun ConversationSettingsBottomSheet(
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
        ConversationItem(
            title = stringResource(R.string.change_chat_key_model),
            icon = Icons.Rounded.Tune,
            isDestructive = false,
            onClick = onChangeModelClicked,
        )
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
            ConversationSettingsBottomSheet(
                onRenameClicked = {},
                onChangeKeyClicked = {},
                onChangeModelClicked = {},
                onDeleteClicked = {},
            )
        }
    }
}