package com.eltex.androidschool.feature.posts

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
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eltex.androidschool.R
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.theme.ComposeAppTheme

@Composable
fun CardPost(
    post: PostUiModel,
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCommentClick: () -> Unit,
    onAudioClick: () -> Unit,
) {
    Card(modifier.fillMaxWidth()) {
        Column {
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = post.author.firstOrNull()?.uppercase().orEmpty()
                    )
                    AsyncImage(
                        modifier = Modifier
                            .size(40.dp),
                        model = post.authorAvatar,
                        contentDescription = null,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(Modifier.weight(1F)) {
                    Text(text = post.author)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = post.published)
                }

                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = null,
                    )

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.delete))
                            },
                            onClick = onDeleteClick,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            post.attachment?.let {
                when (post.attachment.attachmentType) {
                    AttachmentType.IMAGE ->
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth(),
                            model = it.url,
                            contentDescription = null,
                        )

                    AttachmentType.VIDEO -> {}
                    AttachmentType.AUDIO -> {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Spacer(
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(horizontal = 16.dp)
                            )
                            IconButton(
                                onClick = onAudioClick,
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Icon(
                                    modifier = Modifier,
                                    painter = if (!post.playedAudio) {
                                        painterResource(id = R.drawable.baseline_play_circle_filled_24)
                                    } else {
                                        painterResource(id = R.drawable.baseline_pause_circle_filled_24)
                                    },
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
            }

            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 32.dp,
                ),
                text = post.content,
            )

            Row(Modifier.padding(horizontal = 16.dp)) {
                TextButton(onClick = onLikeClick) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(
                            id = if (post.likedByMe) {
                                R.drawable.baseline_favorite_24
                            } else {
                                R.drawable.baseline_favorite_border_24
                            }
                        ),
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = post.likes.toString(), color = MaterialTheme.colorScheme.primary)
                }

                IconButton(onClick = onShareClick) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        painter = painterResource(id = R.drawable.baseline_share_24),
                        contentDescription = null,
                    )
                }

                IconButton(onClick = onCommentClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_comment_24),
                        contentDescription = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun CardPostPreview() {
    CardPost(
        post = PostUiModel(
            content = "Шляпа — это головной убор, который носили в Древней Греции. В наше время шляпы носят для защиты от солнца или просто для красоты.",
            author = "Leo Lipshutz",
            published = "21.02.22 14:23",
        ),
        onLikeClick = {},
        onShareClick = {},
        onDeleteClick = {},
        onCommentClick = {},
        onAudioClick = {},
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardPostPreviewDark() {
    ComposeAppTheme {
        CardPost(
            post = PostUiModel(
                content = "Шляпа — это головной убор, который носили в Древней Греции. В наше время шляпы носят для защиты от солнца или просто для красоты.",
                author = "Leo Lipshutz",
                published = "21.02.22 14:23"
            ),
            onLikeClick = {},
            onShareClick = {},
            onDeleteClick = {},
            onCommentClick = {},
            onAudioClick = {},
        )
    }
}
