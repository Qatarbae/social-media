package com.eltex.androidschool.feature.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.androidschool.R
import com.eltex.androidschool.feature.comments.effecthandler.CommentEffectHandler
import com.eltex.androidschool.feature.comments.mapper.CommentUiModelMapper
import com.eltex.androidschool.feature.comments.model.CommentMessage
import com.eltex.androidschool.feature.comments.model.CommentStatus
import com.eltex.androidschool.feature.comments.model.CommentUiModel
import com.eltex.androidschool.feature.comments.repository.CommentRepository
import com.eltex.androidschool.feature.comments.store.CommentStore
import com.eltex.androidschool.feature.comments.viewmodel.CommentUiState
import com.eltex.androidschool.feature.comments.viewmodel.CommentViewModel
import com.eltex.androidschool.feature.paging.ItemError
import kotlinx.coroutines.flow.filter

@Composable
fun CommentsScreen(
    modifier: Modifier = Modifier,
    viewModel: CommentViewModel = viewModel(),
    postId: Long? = null,
) {
    val state by viewModel.uiState.collectAsState()

    val commentListener = object : CommentListener {

        override fun onLikeClicked(comment: CommentUiModel) {
            postId?.let {
                viewModel.accept(CommentMessage.Like(postId, comment))
            }
        }

        override fun onDeleteClicked(comment: CommentUiModel) {
            postId?.let {
                viewModel.accept(CommentMessage.Delete(postId, comment))
            }
        }

        override fun onRefresh() {
            postId?.let {
                viewModel.accept(CommentMessage.Refresh(postId))
            }
        }

        override fun retryClicked() {
            postId?.let {
                viewModel.accept(CommentMessage.Retry)
            }
        }
    }

    val onCreateClicked = { comment: CommentUiModel ->
        postId?.let {
            viewModel.accept(CommentMessage.Create(postId, comment))
        } ?: Unit
    }

    when (val status = state.status) {
        is CommentStatus.EmptyError -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ItemError(error = status.reason) {
                    postId?.let {
                        viewModel.accept(CommentMessage.Refresh(postId))
                    }
                }
            }
        }

        CommentStatus.EmptyLoading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> {
            Column(modifier = modifier.fillMaxSize()) {
                if (state.comments.isEmpty()) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.noComments))
                    }
                } else {
                    CommentsListBlock(
                        state.comments,
                        status is CommentStatus.Refreshing,
                        commentListener,
                    )
                }
                CommentCreateBlock(onCreateClicked)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColumnScope.CommentsListBlock(
    items: List<CommentUiModel>,
    refreshing: Boolean,
    listener: CommentListener,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, { listener.onRefresh() })
    Box(
        modifier = modifier
            .fillMaxSize()
            .weight(1f)
            .pullRefresh(pullRefreshState)
    ) {
        val listState = rememberLazyListState()

        LaunchedEffect(listState) {
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
            }
                .filter { it }
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            items(items, key = {
                it.id.toString()
            }) { comment ->
                CardComment(
                    comment = comment,
                    onLikeClick = { listener.onLikeClicked(comment) },
                    onDeleteClick = {
                        listener.onDeleteClicked(comment)
                    },
                )
                Spacer(modifier = modifier.height(8.dp))
            }
        }

        PullRefreshIndicator(refreshing, pullRefreshState, modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun CommentCreateBlock(
    onCreateClicked: (CommentUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            //height(intrinsicSize = IntrinsicSize.Min)
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        TextField(
            value = value,
            onValueChange = { text: TextFieldValue -> value = text },
            placeholder = @Composable {
                Text(
                    stringResource(id = R.string.enterTextHere)
                )
            },
            trailingIcon = {
                if (value.text.isNotBlank()) {
                    IconButton(
//                        modifier = modifier.fillMaxHeight(),
                        onClick = {
                            onCreateClicked(CommentUiModel(content = value.text))
                            value = TextFieldValue()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_send_24),
                            contentDescription = null
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                errorContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
//                .shadow(elevation = 8.dp, shape = CircleShape)
        )
    }
}

@Composable
private fun CommentsScreenPreview(state: CommentUiState) {
//    ComposeAppTheme {
    CommentsScreen(
        viewModel = CommentViewModel(
            CommentStore(
                CommentReducer(),
                CommentEffectHandler(
                    object : CommentRepository {},
                    CommentUiModelMapper(),
                ),
                initState = state,
            )
        ),
    )
//    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CommentScreenEmptyLoadingPreview() {
    CommentsScreenPreview(
        state = CommentUiState(
            emptyList(),
            CommentStatus.EmptyLoading
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CommentScreenEmptyErrorPreview() {
    CommentsScreenPreview(
        state = CommentUiState(
            emptyList(),
            CommentStatus.EmptyError(RuntimeException("Test"))
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CommentScreenIdleEmptyPreview() {
    CommentsScreenPreview(
        state = CommentUiState(
            emptyList()
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CommentScreenIdlePreview() {
    CommentsScreenPreview(
        state = CommentUiState(
            List(10) {
                CommentUiModel(
                    id = it + 1L,
                    author = "Leo Lipshutz",
                    published = "21.02.22 14:23",
                    content = "Отличный пост!"
                )
            },
        )
    )
}