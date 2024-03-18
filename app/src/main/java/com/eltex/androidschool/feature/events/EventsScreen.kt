package com.eltex.androidschool.feature.events

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.androidschool.R
import com.eltex.androidschool.di.DateTimeFormatter
import com.eltex.androidschool.effecthandler.EventEffectHandler
import com.eltex.androidschool.feature.paging.CardSkeletonEvent
import com.eltex.androidschool.feature.paging.ItemError
import com.eltex.androidschool.mapper.EventPagingModelMapper
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventStatus
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.reducer.EventReducer
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.theme.ComposeAppTheme
import com.eltex.androidschool.viewmodel.EventStore
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.filter
import java.time.ZoneId

@Composable
fun EventsScreen(
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = viewModel(),
    listener: EventNavigationListener
) {
    val state by viewModel.state.collectAsState()
    val mapper = remember { EventPagingModelMapper() }
    val context = LocalContext.current

    val eventListener = object : EventListener {
        override fun onEventDetailsClickListener(event: EventUiModel) {
            listener.onEventDetailsClickListener(R.id.eventDetailsFragment, event)
        }

        override fun onLikeClickListener(event: EventUiModel) {
            viewModel.accept(EventMessage.Like(event))
        }

        override fun onShareClickListener(event: EventUiModel) {
            val intent = Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, event.content)
                .setType("text/plain")

            val chooser = Intent.createChooser(intent, null)
            context.startActivity(chooser)
        }

        override fun onDeleteClickListener(event: EventUiModel) {
            viewModel.accept(EventMessage.Delete(event))
        }

        override fun onEditClickListener(event: EventUiModel) {
            listener.onEditClickListener(R.id.editEventFragment, event)
        }

        override fun onParticipateClickListener(event: EventUiModel) {
            viewModel.accept(EventMessage.Participate(event))
        }

        override fun onAudioClickListener(event: EventUiModel) {
            viewModel.togglePlay(event)
            viewModel.accept(EventMessage.AudioButtonClick(event))
        }

        override fun onRetryClickListener() {
            viewModel.accept(EventMessage.Retry)
        }

        override fun onRefresh() {
            viewModel.accept(EventMessage.Refresh)
        }

        override fun loadNextPage() {
            viewModel.accept(EventMessage.LoadNextPage)
        }

        override fun loadPrevPage() {
            viewModel.accept((EventMessage.LoadPrevPage))
        }
    }

    val listState: LazyListState = rememberLazyListState()


    when (val status = state.status) {
        is EventStatus.EmptyError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ItemError(error = status.reason) {
                    viewModel.accept(EventMessage.Refresh)
                }
            }
        }

        else -> EventsListScreen(
            items = mapper.map(state),
            refreshing =  status is EventStatus.Refreshing,
            listener = eventListener,
            listState =  listState
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EventsListScreen(
    items: List<PagingModel<EventUiModel>>,
    refreshing: Boolean,
    listener: EventListener,
    modifier: Modifier = Modifier,
    listState: LazyListState
) {
    val pullRefreshState =
        rememberPullRefreshState(refreshing = refreshing, onRefresh = { listener.onRefresh() })

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
//        val listState = rememberLazyListState()
        LaunchedEffect(listState) {
            snapshotFlow {
                val position = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val itemsCount = listState.layoutInfo.totalItemsCount
                (position == itemsCount - 1) || (position == itemsCount - 4)
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
                    is PagingModel.Loading -> it.value.toString()
                }
            }) { item ->
                when (item) {
                    is PagingModel.Data -> {
                        val event = item.value
                        CardEvent(
                            modifier = Modifier.clickable {
                                listener.onEventDetailsClickListener(event)
                            },
                            event = event,
                            onDeleteClickListener = { listener.onDeleteClickListener(event) },
                            onEditClickListener = { listener.onEditClickListener(event) },
                            onLikeClickListener = { listener.onLikeClickListener(event) },
                            onShareClickListener = { listener.onShareClickListener(event) },
                            onParticipateClickListener = { listener.onParticipateClickListener(event) },
                            onAudioClickListener = { listener.onAudioClickListener(event) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    is PagingModel.Error -> ItemError(error = item.reason) {
                        listener.onRetryClickListener()
                    }

                    is PagingModel.Loading -> {
                        CardSkeletonEvent()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing, state = pullRefreshState, Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}

@Composable
private fun EventsScreenPreview(state: EventUiState) {
    ComposeAppTheme {
        EventsScreen(
            viewModel = EventViewModel(
                EventStore(
                    EventReducer(),
                    EventEffectHandler(
                        object : EventRepository {},
                        EventUiModelMapper(DateTimeFormatter(ZoneId.systemDefault()))
                    ),
                    initState = state
                ), application = Application()
            ),
            listener = object : EventNavigationListener {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EventScreenEmptyLoadingPreview() {
    EventsScreenPreview(state = EventUiState(emptyList(), 10, status = EventStatus.InitialLoading))
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EventScreenEmptyErrorPreview() {
    EventsScreenPreview(
        state = EventUiState(
            emptyList(),
            status = EventStatus.EmptyError(RuntimeException("Test"))
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EventScreenIdlePreview() {
    EventsScreenPreview(
        state = EventUiState(
            List(10) {
                EventUiModel(
                    content = stringResource(id = R.string.skeleton_content),
                    author = stringResource(id = R.string.skeleton_author),
                    published = stringResource(id = R.string.skeleton_published),
                    type = stringResource(id = R.string.skeleton_type),
                    datetime = stringResource(id = R.string.skeleton_datetime),
                    link = stringResource(id = R.string.skeleton_link),
                    likes = stringResource(id = R.string.skeleton_likes).toInt(),
                    likedByMe = true,
                    participants = stringResource(id = R.string.skeleton_participants).toInt(),
                )
            },
        )
    )
}
