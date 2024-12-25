package com.itsdecker.androidai.screens.addkey

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.data.ModelField
import com.itsdecker.androidai.data.SupportedProvider
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.spacing

const val WIDTH_FRACTION = 0.7f

@Composable
fun AddKeyScreen(
    viewModel: AddKeyViewModel,
    modifier: Modifier,
) {
    val selectedProvider = viewModel.modelSelection.collectAsState()
    val currentField = viewModel.currentField.collectAsState()

    BackHandler(
        enabled = selectedProvider.value != null,
    ) {
        viewModel.goBack()
    }

    if (selectedProvider.value == null) {
        ProviderSelection(
            supportedProviders = viewModel.supportedProviders,
            onProviderSelected = { providerSelection -> viewModel.setProvider(providerSelection) },
            modifier = modifier,
        )
    } else if (currentField.value != null) {
        (currentField.value)?.let { formField ->
            SetFieldStep(
                field = formField,
                onTextValueChange = { textValue -> viewModel.updateTextValue(textValue) },
                onGoBack = { viewModel.goBack() },
                onFieldSubmit = { viewModel.submitCurrentField() },
                modifier = modifier,
            )
        }

    } else {
        SubmissionStep(
            goBackClicked = { viewModel.goBack() },
            saveModelClicked = { viewModel.saveModel() },
            modifier = modifier,
        )
    }
}

@Composable
fun ProviderSelection(
    supportedProviders: List<SupportedProvider>,
    onProviderSelected: (SupportedProvider) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.spacing.default,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Select a Provider",
            style = Typography.titleLarge,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(fraction = WIDTH_FRACTION)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            text = "Select the provider to get started",
            style = Typography.bodySmall,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        for(model in supportedProviders) {
            ProviderButton(
                providerIcon = model.icon,
                providerColor = model.brandColor,
                providerName = model.providerName,
                onClick = { onProviderSelected(model) },
                modifier = Modifier
                    .fillMaxWidth(WIDTH_FRACTION)
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
fun ProviderButton(
    @DrawableRes providerIcon: Int,
    providerColor: Color,
    providerName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Icon(
            painter = painterResource(providerIcon),
            contentDescription = null,
            tint = providerColor,
        )
        Text(
            text = providerName,
            style = Typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun SetFieldStep(
    field: ModelField,
    onTextValueChange: (String) -> Unit,
    onGoBack: () -> Unit,
    onFieldSubmit: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.spacing.default,
            Alignment.CenterVertically,
        ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = field.name,
            style = Typography.titleLarge
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(fraction = WIDTH_FRACTION)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            text = field.details,
            style = Typography.bodySmall
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        when (field) {
            is ModelField.Text -> {
                TextFormField(
                    field.value,
                    onValueChange = onTextValueChange,
                    onGoBack = onGoBack,
                    onFieldSubmit = onFieldSubmit,
                    modifier = Modifier
                        .fillMaxWidth(WIDTH_FRACTION)
                        .align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}

@Composable
fun TextFormField(
    value: String,
    onValueChange: (String) -> Unit,
    onGoBack: () -> Unit,
    onFieldSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.default)
        ) {
            OutlinedButton(
                onClick = onGoBack,
                modifier = Modifier.weight(1f),
            ) {
                Text("Previous")
            }
            OutlinedButton(
                onClick = onFieldSubmit,
                modifier = Modifier.weight(1f),
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun SubmissionStep(
    goBackClicked: () -> Unit,
    saveModelClicked: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.spacing.default,
            Alignment.CenterVertically,
        ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Ready To Go?",
            style = Typography.titleLarge
        )

        Spacer(modifier = Modifier.height(0.dp))

        OutlinedButton(
            onClick = goBackClicked,
            modifier = Modifier
                .fillMaxWidth(WIDTH_FRACTION)
                .align(Alignment.CenterHorizontally),
        ) {
            Text("Go Back")
        }

        OutlinedButton(
            onClick = saveModelClicked,
            modifier = Modifier
                .fillMaxWidth(WIDTH_FRACTION)
                .align(Alignment.CenterHorizontally),
        ) {
            Text("Save API Key")
        }
    }
}