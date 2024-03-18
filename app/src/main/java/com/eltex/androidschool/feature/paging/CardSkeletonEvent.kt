package com.eltex.androidschool.feature.paging

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eltex.androidschool.R
import com.eltex.androidschool.theme.ComposeAppTheme

val COMMON_SPACING = 16.dp
val BIG_SPACING = 32.dp

@Composable
fun CardSkeletonEvent(
    modifier: Modifier = Modifier,
) {
    val skeletonColor = MaterialTheme.colorScheme.outlineVariant

    Card(modifier.fillMaxWidth()) {
        Column {
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(COMMON_SPACING))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(skeletonColor),
                )

                Spacer(modifier = Modifier.width(COMMON_SPACING))

                Column(Modifier.weight(1F)) {
                    Text(
                        text = stringResource(id = R.string.skeleton_author),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = skeletonColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(skeletonColor)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.skeleton_published),
                        color = skeletonColor,
                        modifier = Modifier.background(skeletonColor)
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier
                .padding(top = COMMON_SPACING, start = COMMON_SPACING)
                .background(skeletonColor),
            text = stringResource(id = R.string.skeleton_type),
            color = skeletonColor,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier
                .padding(start = COMMON_SPACING)
                .background(skeletonColor),
            text = stringResource(id = R.string.skeleton_datetime),
            color = skeletonColor
        )

        Text(
            modifier = Modifier
                .padding(horizontal = COMMON_SPACING, vertical = BIG_SPACING)
                .background(skeletonColor),
            text = stringResource(id = R.string.skeleton_content),
            color = skeletonColor
        )

        Text(
            modifier = Modifier
                .padding(horizontal = COMMON_SPACING)
                .fillMaxWidth()
                .background(skeletonColor),
            text = stringResource(id = R.string.skeleton_link),
            color = skeletonColor
        )

        Row(
            modifier = Modifier.padding(
                start = COMMON_SPACING,
                end = COMMON_SPACING,
                top = BIG_SPACING,
                bottom = COMMON_SPACING
            )
        ) {
            TextButton(onClick = {}) {
                Icon(
                    tint = skeletonColor,
                    painter = painterResource(
                        id = R.drawable.baseline_favorite_24,
                    ), contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(text = stringResource(id = R.string.skeleton_likes), color = skeletonColor)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        tint = skeletonColor,
                        painter = painterResource(id = R.drawable.baseline_share_24),
                        contentDescription = null
                    )
                }

                TextButton(onClick = {}) {
                    Icon(
                        tint = skeletonColor,
                        painter = painterResource(
                            id = R.drawable.baseline_people_outline_24,
                        ), contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = stringResource(id = R.string.skeleton_participants),
                        color = skeletonColor
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CardSkeletonEventPreview() {
    CardSkeletonEvent()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardSkeletonEventPreviewDark() {
    ComposeAppTheme {
        CardSkeletonEvent()
    }
}