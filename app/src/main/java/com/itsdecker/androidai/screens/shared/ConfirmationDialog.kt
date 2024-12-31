package com.itsdecker.androidai.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import com.itsdecker.androidai.R
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.cornerRadius
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmationText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(cornerRadius.large)
                )
                .padding(spacing.medium)
        ) {
            Text(
                text = title,
                style = Typography.titleLarge,
                color = colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = message,
                style = Typography.bodyMedium,
                color = colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                TextButton(
                    onClick = onDismiss,
                ) {
                    Text(text = stringResource(R.string.cancel_option))
                }

                Button(
                    onClick = onConfirm,
                ) {
                    Text(text = confirmationText)
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    deletionTargetText: String,
    onDeleteConfirmed: () -> Unit,
    onDismiss: () -> Unit,
) {
    ConfirmationDialog(
        title = stringResource(R.string.delete_confirmation_title, deletionTargetText),
        message = stringResource(R.string.delete_confirmation_details, deletionTargetText),
        confirmationText = stringResource(R.string.delete_option),
        onConfirm = onDeleteConfirmed,
        onDismiss = onDismiss,
    )
}


@PreviewLightDark
@Composable
fun ConfirmationDialogPreview() {
    AndroidaiTheme {
        DeleteConfirmationDialog(
            deletionTargetText = "My Api Key",
            onDeleteConfirmed = {},
            onDismiss = {}
        )
    }
}