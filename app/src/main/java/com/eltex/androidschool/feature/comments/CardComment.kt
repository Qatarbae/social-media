package com.eltex.androidschool.feature.comments

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import com.eltex.androidschool.feature.comments.model.CommentUiModel
import com.eltex.androidschool.theme.ComposeAppTheme

@Composable
fun CardComment(
    comment: CommentUiModel,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    onLikeClick: () -> Unit,
) {
    Card(modifier.fillMaxWidth()) {
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
                    text = comment.author.firstOrNull()?.uppercase().orEmpty()
                )
                AsyncImage(
                    modifier = Modifier
                        .size(40.dp),
                    model = comment.authorAvatar,
                    contentDescription = null,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(Modifier.weight(1F)) {
                Text(text = comment.author)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = comment.published)
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

        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 32.dp,
            ),
            text = comment.content,
        )

        Row(Modifier.padding(horizontal = 16.dp)) {
            TextButton(onClick = onLikeClick) {
                Icon(
                    painter = painterResource(
                        id = if (comment.likedByMe) {
                            R.drawable.baseline_favorite_24
                        } else {
                            R.drawable.baseline_favorite_border_24
                        }

                    ),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = comment.likes.toString())
            }
        }
    }
}

@Preview
@Composable
fun CardCommentPreview() {
    CardComment(
        comment = CommentUiModel(
            author = "Leo Lipshutz",
            published = "21.02.22 14:23",
            content = "Отличный пост",
        ),
        onDeleteClick = {},
        onLikeClick = {},
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardCommentPreviewDark() {
    ComposeAppTheme {
        CardComment(
            comment = CommentUiModel(
                author = "Leo Lipshutz",
                published = "21.02.22 14:23",
                content = "Отличный пост",
            ),
            onDeleteClick = {},
            onLikeClick = {},
        )
    }
}