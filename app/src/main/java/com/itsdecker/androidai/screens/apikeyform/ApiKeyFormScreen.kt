package com.itsdecker.androidai.screens.apikeyform

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.R
import com.itsdecker.androidai.data.SUPPORTED_PROVIDERS
import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.database.ApiKeyEntity
import com.itsdecker.androidai.screens.preview.apiKeyEntityPreview
import com.itsdecker.androidai.screens.shared.DeleteConfirmationDialog
import com.itsdecker.androidai.screens.shared.FormField
import com.itsdecker.androidai.screens.shared.FormSubcontentText
import com.itsdecker.androidai.screens.shared.FormTextInput
import com.itsdecker.androidai.screens.shared.ScreenHeader
import com.itsdecker.androidai.screens.shared.ScrollableContainer
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun ApiKeyFormScreen(
    modifier: Modifier = Modifier,
    viewModel: ApiKeyViewModel,
) {
    val context = LocalContext.current
    val apiKeyEntity = viewModel.apiKeyEntity.collectAsState()
    val isDefaultKey = viewModel.isDefaultKey.collectAsState()
    val userAgreementChecked = viewModel.userAgreementChecked.collectAsState()

    ApiKeyFormScreen(
        modifier = modifier,
        apiKeyEntity = apiKeyEntity.value,
        isDefaultKey = isDefaultKey.value,
        userAgreementChecked = userAgreementChecked.value,
        isExistingKey = viewModel.isExistingKey(),
        canSave = viewModel.canSave(),
        onProviderSelected = { provider -> viewModel.onProviderSelected(provider, context) },
        onNameChanged = viewModel::onNameChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onApiKeyChanged = viewModel::onApiKeyChanged,
        onDefaultKeyChanged = viewModel::onDefaultKeyChanged,
        onUserAgreementChanged = viewModel::onUserAgreementChanged,
        onSaveClick = viewModel::saveModel,
        onCancelClick = viewModel::cancel,
        onDeleteClick = viewModel::deleteModel,
    )
}

@Composable
fun ApiKeyFormScreen(
    modifier: Modifier = Modifier,
    apiKeyEntity: ApiKeyEntity,
    isDefaultKey: Boolean,
    userAgreementChecked: Boolean,
    isExistingKey: Boolean,
    canSave: Boolean,
    onProviderSelected: (provider: SupportedProvider) -> Unit,
    onNameChanged: (title: String) -> Unit,
    onDescriptionChanged: (description: String) -> Unit,
    onApiKeyChanged: (apiKey: String) -> Unit,
    onDefaultKeyChanged: (selected: Boolean) -> Unit,
    onUserAgreementChanged: (selected: Boolean) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val showDeleteConformationDialog = remember { mutableStateOf(false) }

    AndroidaiTheme {
        Column(
            modifier = modifier
                .background(color = colorScheme.surface)
                .systemBarsPadding()
        ) {
            ScreenHeader(
                title = if (isExistingKey) {
                    stringResource(R.string.update_key, apiKeyEntity.name)
                } else stringResource(R.string.add_new_key),
                subtitle = null,
                leadingIcon = {
                    IconButton(
                        onClick = onCancelClick
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.close_option),
                            tint = colorScheme.onSurface,
                        )
                    }
                }
            )

            ApiKeyFields(
                modifier = Modifier.weight(1f),
                isExistingKey = isExistingKey,
                apiKeyEntity = apiKeyEntity,
                isDefaultKey = isDefaultKey,
                userAgreementChecked = userAgreementChecked,
                onProviderSelected = onProviderSelected,
                onNameChanged = onNameChanged,
                onDescriptionChanged = onDescriptionChanged,
                onApiKeyChanged = onApiKeyChanged,
                onDefaultKeyChanged = onDefaultKeyChanged,
                onUserAgreementChanged = onUserAgreementChanged,
            )

            ApiKeyButtons(
                canSave = canSave,
                onSaveClick = onSaveClick,
                onCancelClick = onCancelClick,
                onDeleteClick = {
                    showDeleteConformationDialog.value = true
                }.takeIf { isExistingKey },
            )
        }

        if (showDeleteConformationDialog.value) {
            DeleteConfirmationDialog(
                deletionTargetText = "\"${apiKeyEntity.name}\"",
                onDeleteConfirmed = {
                    showDeleteConformationDialog.value = false
                    onDeleteClick.invoke()
                },
                onDismiss = { showDeleteConformationDialog.value = false }
            )
        }
    }
}

@Composable
fun ApiKeyFields(
    modifier: Modifier,
    isExistingKey: Boolean,
    apiKeyEntity: ApiKeyEntity,
    isDefaultKey: Boolean,
    userAgreementChecked: Boolean,
    onProviderSelected: (provider: SupportedProvider) -> Unit,
    onNameChanged: (title: String) -> Unit,
    onDescriptionChanged: (description: String) -> Unit,
    onApiKeyChanged: (apiKey: String) -> Unit,
    onDefaultKeyChanged: (selected: Boolean) -> Unit,
    onUserAgreementChanged: (selected: Boolean) -> Unit,
) {
    val scrollState = rememberScrollState()

    ScrollableContainer(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState),
        ) {
            // Providers
            FormField(
                title = stringResource(R.string.api_provider_field),
                details = stringResource(R.string.api_provider_details),
                fieldView = {
                    FormFieldProviderChips(
                        modifier = Modifier.fillMaxWidth(),
                        selectedProvider = apiKeyEntity.chatModel,
                        supportedProviders = SUPPORTED_PROVIDERS,
                        onProviderSelected = onProviderSelected,
                    )
                },
            )

            // Name
            FormField(
                title = stringResource(R.string.name_field),
                fieldView = {
                    FormTextInput(
                        modifier = Modifier.fillMaxWidth(),
                        text = apiKeyEntity.name,
                        maxCharacters = 32,
                        maxLines = 2,
                        onValueChanged = onNameChanged,
                    )
                }
            )

            // Description
            FormField(
                title = stringResource(R.string.description_field),
                details = stringResource(R.string.api_key_description_details),
                fieldView = {
                    FormTextInput(
                        modifier = Modifier.fillMaxWidth(),
                        text = apiKeyEntity.description,
                        maxCharacters = 128,
                        maxLines = 3,
                        onValueChanged = onDescriptionChanged,
                    )
                }
            )

            // API Key
            FormField(
                title = stringResource(R.string.api_key_field),
                details = stringResource(R.string.api_key_details),
                fieldView = {
                    FormTextInput(
                        modifier = Modifier.fillMaxWidth(),
                        text = apiKeyEntity.apiKey,
                        obfuscate = isExistingKey,
                        enabled = !isExistingKey,
                        maxLines = 5,
                        onValueChanged = onApiKeyChanged,
                    )
                },
                subContentView = {
                    ApiKeyLink(linkText = apiKeyEntity.chatModel.apiKeyLink)
                }
            )

            // Default
            FormField(
                title = stringResource(R.string.default_key_field),
                details = stringResource(R.string.default_key_details),
                trailingContentView = {
                    Checkbox(
                        modifier = Modifier.padding(end = spacing.small),
                        checked = isDefaultKey,
                        onCheckedChange = onDefaultKeyChanged,
                    )
                },
            )

            // Agreement
            FormField(
                title = stringResource(R.string.api_key_use_field),
                details = stringResource(R.string.api_key_use_agreement),
                trailingContentView = {
                    Checkbox(
                        modifier = Modifier.padding(end = spacing.small),
                        checked = userAgreementChecked,
                        onCheckedChange = onUserAgreementChanged,
                        enabled = !isExistingKey,
                    )
                }
            )
        }
    }
}

@Composable
fun ApiKeyButtons(
    canSave: Boolean,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(all = spacing.default),
        horizontalArrangement = Arrangement.spacedBy(spacing.default),
    ) {

        onDeleteClick?.let {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onDeleteClick,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = colorScheme.errorContainer,
                    contentColor = colorScheme.onErrorContainer,
                ),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(R.string.delete_option),
                )

                Spacer(modifier = Modifier.size(spacing.small))

                Text(
                    text = stringResource(R.string.delete_option),
                    maxLines = 1,
                )
            }
        }

        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onCancelClick,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        ) {
            Icon(
                imageVector = Icons.Rounded.Cancel,
                contentDescription = stringResource(R.string.cancel_option),
            )

            Spacer(modifier = Modifier.size(spacing.small))

            Text(
                text = stringResource(R.string.cancel_option),
                maxLines = 1,
            )
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = onSaveClick,
            enabled = canSave,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        ) {
            Icon(
                imageVector = Icons.Rounded.Save,
                contentDescription = stringResource(R.string.save_option),
            )

            Spacer(modifier = Modifier.size(spacing.small))

            Text(
                text = stringResource(R.string.save_option),
                maxLines = 1,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormFieldProviderChips(
    modifier: Modifier = Modifier,
    selectedProvider: SupportedProvider?,
    supportedProviders: List<SupportedProvider>,
    onProviderSelected: (provider: SupportedProvider) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.small),
        verticalArrangement = Arrangement.spacedBy(spacing.small),
    ) {
        supportedProviders.forEach { provider ->
            val isSelected = provider == selectedProvider

            val (chipModifier, contentColor) = if (isSelected) {
                Modifier
                    .background(
                        color = colorScheme.primary,
                        shape = CircleShape,
                    ) to colorScheme.onPrimary
            } else {
                Modifier
                    .border(
                        width = 1.dp,
                        color = colorScheme.outline,
                        shape = CircleShape,
                    ) to colorScheme.onSurface
            }

            Row(
                modifier = chipModifier
                    .clickable { onProviderSelected(provider) }
                    .padding(vertical = spacing.small, horizontal = spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
            ) {
                Icon(
                    painter = painterResource(provider.icon),
                    contentDescription = provider.providerName,
                    tint = provider.brandColor,
                    modifier = Modifier.size(20.dp),
                )

                Text(
                    text = provider.providerName,
                    style = Typography.bodyMedium,
                    color = contentColor,
                )
            }
        }
    }
}

@Composable
fun ApiKeyLink(
    linkText: String,
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        FormSubcontentText(
            text = stringResource(R.string.api_key_help_link),
        )
        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = linkText.isNotBlank(),
            onClick = { uriHandler.openUri(linkText) },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = stringResource(R.string.get_api_key_button),
            )
            Spacer(modifier = Modifier.size(spacing.small))
            Text(
                text = stringResource(R.string.get_api_key_button),
            )

        }
    }
}

@PreviewLightDark
@Composable
fun TextInputPreview() {
    AndroidaiTheme {
        ApiKeyFormScreen(
            apiKeyEntity = apiKeyEntityPreview(),
            isDefaultKey = true,
            isExistingKey = true,
            userAgreementChecked = true,
            canSave = true,
            onProviderSelected = {},
            onNameChanged = {},
            onDescriptionChanged = {},
            onApiKeyChanged = {},
            onDefaultKeyChanged = {},
            onUserAgreementChanged = {},
            onSaveClick = {},
            onDeleteClick = {},
            onCancelClick = {},
        )
    }
}