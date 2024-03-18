package com.eltex.androidschool.feature.posts

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.androidschool.effecthandler.PostEffectHandler
import com.eltex.androidschool.feature.paging.ItemError
import com.eltex.androidschool.feature.paging.ItemProgress
import com.eltex.androidschool.mapper.PostPagingModelMapper
import com.eltex.androidschool.mapper.PostUiModelMapper
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.model.PostMessage
import com.eltex.androidschool.model.PostStatus
import com.eltex.androidschool.model.PostUiModel
import com.eltex.androidschool.model.PostUiState
import com.eltex.androidschool.reducer.PostReducer
import com.eltex.androidschool.repository.postrepository.PostRepository
import com.eltex.androidschool.theme.ComposeAppTheme
import com.eltex.androidschool.viewmodel.PostStore
import com.eltex.androidschool.viewmodel.PostViewModel
import kotlinx.coroutines.flow.filter

@Composable
fun PostScreen(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = viewModel(),
    onCommentClicked: (PostUiModel) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val mapper = remember { PostPagingModelMapper() }
    val context = LocalContext.current

    val postListener = object : PostListener {
        override fun onShareClicked(post: PostUiModel) {
            val intent = Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, post.content)
                .setType("text/plain")

            val chooser = Intent.createChooser(intent, null)

            context.startActivity(chooser)
        }

        override fun onLikeClicked(post: PostUiModel) {
            viewModel.accept(PostMessage.Like(post))
        }

        override fun onDeleteClicked(post: PostUiModel) {
            viewModel.accept(PostMessage.Delete(post))
        }

        override fun onCommentClicked(post: PostUiModel) = onCommentClicked(post)

        override fun onRefresh() {
            viewModel.accept(PostMessage.Refresh)
        }

        override fun retryClicked() {
            viewModel.accept(PostMessage.Retry)
        }

        override fun onAudioClicked(post: PostUiModel) {
            viewModel.accept(PostMessage.AudioButtonClick(post))
            viewModel.togglePlay(post)
        }

        override fun loadNextPage() {
            viewModel.accept(PostMessage.LoadNextPage)
        }

        override fun loadPrevPage() {
            viewModel.accept(PostMessage.LoadPrevPage)
        }
    }

    when (val status = state.status) {
        is PostStatus.EmptyError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ItemError(error = status.reason) {
                    viewModel.accept(PostMessage.Refresh)
                }
            }
        }

        PostStatus.EmptyLoading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> PostsListScreen(
            mapper.map(state),
            status is PostStatus.Refreshing,
            postListener, modifier,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PostsListScreen(
    items: List<PagingModel<PostUiModel>>,
    refreshing: Boolean,
    listener: PostListener,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, { listener.onRefresh() })
    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        val listState = rememberLazyListState()

        LaunchedEffect(listState) {
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
            }
                .filter { it }
                .collect {
                    listener.loadNextPage()
                }
        }

        LaunchedEffect(listState) {
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == 0
            }
                .filter { it }
                .collect {
                    listener.loadPrevPage()
                }
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp)
        ) {
            items(items, key = {
                when (it) {
                    is PagingModel.Data -> it.value.id.toString()
                    is PagingModel.Error -> PagingModel.Error::class.simpleName.orEmpty()
                    is PagingModel.Loading -> PagingModel.Loading::class.simpleName.orEmpty()
                }
            }) { item ->
                when (item) {
                    is PagingModel.Data -> {
                        val post = item.value
                        CardPost(
                            post = post,
                            onLikeClick = { listener.onLikeClicked(post) },
                            onShareClick = {
                                listener.onShareClicked(post)
                            },
                            onDeleteClick = {
                                listener.onDeleteClicked(post)
                            },
                            onCommentClick = {
                                listener.onCommentClicked(post)
                            },
                            onAudioClick = {
                                listener.onAudioClicked(post)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    is PagingModel.Error -> ItemError(error = item.reason) {
                        listener.retryClicked()
                    }

                    is PagingModel.Loading -> ItemProgress()
                }
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun PostScreenPreview(state: PostUiState) {
    ComposeAppTheme {
        PostScreen(
            viewModel = PostViewModel(
                PostStore(
                    PostReducer(),
                    PostEffectHandler(
                        object : PostRepository {},
                        PostUiModelMapper(),
                    ),
                    initState = state,
                ), application = Application()
            ),
            onCommentClicked = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PostScreenEmptyLoadingPreview() {
    PostScreenPreview(state = PostUiState(emptyList(), 10 ,PostStatus.EmptyLoading))
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PostScreenEmptyErrorPreview() {
    PostScreenPreview(
        state = PostUiState(
            emptyList(),
            status = PostStatus.EmptyError(RuntimeException("Test"))
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PostScreenIdlePreview() {
    PostScreenPreview(
        state = PostUiState(
            List(10) {
                PostUiModel(
                    id = it + 1L,
                    content = "Шляпа — это головной убор, который носили в Древней Греции. В наше время шляпы носят для защиты от солнца или просто для красоты.",
                    author = "Leo Lipshutz",
                    published = "21.02.22 14:23"
                )
            },
        )
    )
}
