package com.itsdecker.androidai.screens.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.itsdecker.androidai.ui.theme.spacing

@Preview
@Composable
fun IconListItem(
    icon: Painter = painterResource(R.drawable.ic_ai_claude),
    iconTint: Color = Color.Blue,
    title: String = "Title of Item",
    subTitle: String = "Some details about the item",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.clickable { onClick() }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(40.dp).align(Alignment.CenterVertically)
            )
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
fun TitleSubtitleItem(
    title: String = "Title of Item",
    subTitle: String = "Some details about the item",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    TitleSubtitle(
        title = title,
        subTitle = subTitle,
        modifier = modifier
            .padding(MaterialTheme.spacing.default)
            .clickable { onClick() },
    )
}

@Composable
private fun TitleSubtitle(
    title: String = "Title of Item",
    subTitle: String = "Some details about the item",
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(MaterialTheme.spacing.default),
    ) {
        Text(
            text = title,
            style = Typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = subTitle,
            style = Typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}