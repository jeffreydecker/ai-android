package com.itsdecker.androidai.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun ScreenHeader(
    title: String?,
    subtitle: String?,
    subtitleIcon: (@Composable RowScope.() -> Unit)? = null,
    leadingIcon: (@Composable RowScope.() -> Unit)? = null,
    trailingActions: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(color = colorScheme.surface)
            .padding(all = spacing.small),
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
    ) {

        leadingIcon?.let { leadingIcon() }

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = spacing.small)
                .weight(weight = 1f),
        ) {
            title?.let {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            subtitle?.let {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.extraSmall)
                ) {
                    subtitleIcon?.let {
                        subtitleIcon()
                    }
                    Text(
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        trailingActions?.let { trailingActions() }
    }
}