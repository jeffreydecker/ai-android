package com.itsdecker.androidai.screens.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itsdecker.androidai.R
import com.itsdecker.androidai.ui.theme.Typography
import com.itsdecker.androidai.ui.theme.colorScheme
import com.itsdecker.androidai.ui.theme.spacing

@Composable
fun IconListItem(
    icon: Painter? = null,
    iconTint: Color? = null,
    title: String,
    subTitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.clickable { onClick() }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.extraSmall),
        ) {

            icon?.let {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = iconTint ?: colorScheme.tertiary,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            TitleSubtitle(
                title = title,
                subTitle = subTitle,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
            )
        }
    }
}

@Composable
fun TitleSubtitleListItem(
    title: String,
    subTitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TitleSubtitle(
        title = title,
        subTitle = subTitle,
        modifier = modifier
            .padding(spacing.default)
            .clickable { onClick() },
    )
}

@Composable
private fun TitleSubtitle(
    title: String,
    subTitle: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(spacing.default),
    ) {
        Text(
            text = title,
            style = Typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        subTitle?.let {
            Text(
                text = subTitle,
                style = Typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IconListItemPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconListItem(
            icon = painterResource(R.drawable.ic_ai_claude),
            iconTint = Color.Blue,
            title = "Title of Item",
            subTitle = "Some details about the item",
            onClick = {},
            modifier = Modifier,
        )

        IconListItem(
            title = "Title of Item",
            subTitle = "Some details about the item",
            onClick = {},
            modifier = Modifier,
        )

        IconListItem(
            title = "Title of Item",
            onClick = {},
            modifier = Modifier,
        )
    }
}