package com.itsdecker.androidai.screens.shared.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import com.itsdecker.androidai.R
import com.itsdecker.androidai.screens.shared.components.FormField
import com.itsdecker.androidai.screens.shared.components.FormTextInput
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.cornerRadius
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun TextFieldDialog(
    title: String,
    initialInputText: String,
    confirmationText: String,
    maxLines: Int = 1,
    maxCharacters: Int = Int.MAX_VALUE,
    onSaveConfirmed: (newText: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val inputText = remember { mutableStateOf("") }

    LaunchedEffect(true) {
        inputText.value = initialInputText
    }

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
            // Name
            FormField(
                title = title,
                fieldView = {
                    FormTextInput(
                        modifier = Modifier.fillMaxWidth(),
                        text = inputText.value,
                        maxCharacters = maxCharacters,
                        maxLines = maxLines,
                        onValueChanged = { inputText.value = it },
                    )
                }
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
                    onClick = { onSaveConfirmed(inputText.value) },
                ) {
                    Text(text = confirmationText)
                }
            }
        }
    }
}

@Composable
fun RenameDialog(
    initialInputText: String,
    onSaveConfirmed: (newText: String) -> Unit,
    onDismiss: () -> Unit,
) {
    TextFieldDialog(
        title = stringResource(R.string.name_field),
        initialInputText = initialInputText,
        confirmationText = stringResource(R.string.save_option),
        maxLines = 2,
        maxCharacters = 32,
        onSaveConfirmed = onSaveConfirmed,
        onDismiss = onDismiss,
    )
}

@PreviewLightDark
@Composable
fun PreviewUpdateNameDialog() {
    AndroidaiTheme {
        RenameDialog(
            initialInputText = "My New Chat",
            onSaveConfirmed = {},
            onDismiss = {},
        )
    }
}