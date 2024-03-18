package com.eltex.androidschool.feature.events

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eltex.androidschool.R
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.theme.ComposeAppTheme

val COMMON_SPACING = 16.dp
val BIG_SPACING = 32.dp

@Composable
fun CardEvent(
    event: EventUiModel,
    modifier: Modifier = Modifier,
    onDeleteClickListener: () -> Unit,
    onEditClickListener: () -> Unit,
    onLikeClickListener: () -> Unit,
    onShareClickListener: () -> Unit,
    onParticipateClickListener: () -> Unit,
    onAudioClickListener: () -> Unit,
) {
    Card(modifier.fillMaxWidth()) {
        Column {
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(COMMON_SPACING))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = event.author.firstOrNull()?.uppercase().orEmpty(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    AsyncImage(model = event.authorAvatar, contentDescription = null)
                }

                Spacer(modifier = Modifier.width(COMMON_SPACING))

                Column(Modifier.weight(1F)) {
                    Text(text = event.author, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = event.published)
                }

                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = null
                    )

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.delete))
                            },
                            onClick = onDeleteClickListener
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.edit))
                            },
                            onClick = onEditClickListener
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))

            event.attachment?.let {
                when (event.attachment.attachmentType) {
                    AttachmentType.IMAGE ->
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth(),
                            model = it.url,
                            contentDescription = null,
                        )

                    AttachmentType.VIDEO -> {}
                    else -> {}
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                Modifier.padding(horizontal = COMMON_SPACING),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1F)
                        .height(48.dp), verticalArrangement = Arrangement.Center
                ) {
                    Text(modifier = Modifier, text = event.type)
                    Text(modifier = Modifier, text = event.datetime)
                }
                event.attachment?.let {
                    when (event.attachment.attachmentType) {
                        AttachmentType.AUDIO -> {
                            IconButton(
                                onClick = onAudioClickListener,
                                modifier = Modifier
                            ) {
                                Icon(
                                    modifier = Modifier,
                                    painter = if (!event.playedAudio) {
                                        painterResource(id = R.drawable.baseline_play_circle_filled_24)
                                    } else {
                                        painterResource(id = R.drawable.baseline_pause_circle_filled_24)
                                    },
                                    contentDescription = null,
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }

            Text(
                modifier = Modifier.padding(horizontal = COMMON_SPACING, vertical = BIG_SPACING),
                text = event.content
            )

            Text(modifier = Modifier.padding(start = COMMON_SPACING), text = event.link)

            Row(
                modifier = Modifier.padding(
                    start = COMMON_SPACING,
                    end = COMMON_SPACING,
                    top = BIG_SPACING,
                    bottom = COMMON_SPACING
                )
            ) {
                TextButton(onClick = onLikeClickListener) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(
                            id = if (event.likedByMe) {
                                R.drawable.baseline_favorite_24
                            } else {
                                R.drawable.baseline_favorite_border_24
                            },
                        ), contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(text = event.likes.toString(), color = MaterialTheme.colorScheme.primary)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onShareClickListener) {
                        Icon(
                            tint = MaterialTheme.colorScheme.primary,
                            painter = painterResource(id = R.drawable.baseline_share_24),
                            contentDescription = null
                        )
                    }

                    TextButton(onClick = onParticipateClickListener) {
                        Icon(
                            tint = MaterialTheme.colorScheme.primary,
                            painter = painterResource(
                                id = if (event.participatedByMe) {
                                    R.drawable.baseline_people_24
                                } else {
                                    R.drawable.baseline_people_outline_24
                                },
                            ), contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = event.participants.toString(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CardEventPreview() {
    CardEvent(
        event = EventUiModel(
            content = stringResource(id = R.string.skeleton_content),
            author = stringResource(id = R.string.skeleton_author),
            published = stringResource(id = R.string.skeleton_published),
            type = stringResource(id = R.string.skeleton_type),
            datetime = stringResource(id = R.string.skeleton_datetime),
            link = stringResource(id = R.string.skeleton_link),
            likes = stringResource(id = R.string.skeleton_likes).toInt(),
            likedByMe = true,
            participants = stringResource(id = R.string.skeleton_participants).toInt(),
        ),
        onEditClickListener = {},
        onDeleteClickListener = {},
        onShareClickListener = {},
        onLikeClickListener = {},
        onParticipateClickListener = {},
        onAudioClickListener = {}
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardEventPreviewDark() {
    ComposeAppTheme {
        CardEvent(
            event = EventUiModel(
                author = "Leo Lipshutz",
                published = "21.02.22 14:23",
                content = "Шляпа — это головной убор, который носили в Древней Греции. В наше время шляпы носят для защиты от солнца или просто для красоты."
            ),
            onEditClickListener = {},
            onDeleteClickListener = {},
            onShareClickListener = {},
            onLikeClickListener = {},
            onParticipateClickListener = {},
            onAudioClickListener = {}
        )
    }
}