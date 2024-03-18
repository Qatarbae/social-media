package com.eltex.androidschool.feature.users

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.androidschool.effecthandler.UserEffectHandler
import com.eltex.androidschool.feature.paging.ItemError
import com.eltex.androidschool.feature.paging.ItemProgress
import com.eltex.androidschool.mapper.UserPagingModelMapper
import com.eltex.androidschool.mapper.UserUiModelMapper
import com.eltex.androidschool.model.PagingModel
import com.eltex.androidschool.model.UserMessage
import com.eltex.androidschool.model.UserStatus
import com.eltex.androidschool.model.UserUiModel
import com.eltex.androidschool.model.UserUiState
import com.eltex.androidschool.reducer.UserReducer
import com.eltex.androidschool.repository.UserRepository
import com.eltex.androidschool.theme.ComposeAppTheme
import com.eltex.androidschool.viewmodel.UserStore
import com.eltex.androidschool.viewmodel.UserViewModel
import kotlinx.coroutines.flow.filter

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = viewModel(),
    checkable: Boolean = false,
) {
    val state by viewModel.uiState.collectAsState()
    val mapper = remember { UserPagingModelMapper() }

    val userListener = object : UserListener {
        override fun onRefresh() {
            viewModel.accept(UserMessage.Refresh)
        }

        override fun checkedListener(user: UserUiModel) {
            viewModel.accept(UserMessage.Check(user.id))
        }
    }

    when (val status = state.status) {
        is UserStatus.EmptyError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ItemError(error = status.reason) {
                    viewModel.accept(UserMessage.Refresh)
                }
            }
        }

        UserStatus.EmptyLoading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> UsersListScreen(
            mapper.map(state),
            status is UserStatus.Refreshing,
            userListener,
            checkable = checkable,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun UsersListScreen(
    items: List<PagingModel<UserUiModel>>,
    refreshing: Boolean,
    listener: UserListener,
    modifier: Modifier = Modifier,
    checkable: Boolean = false,
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
                    listener.onRefresh()
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
                        val user = item.value
                        CardUserChecked(
                            user = user,
                            checkedListener = {
                                listener.checkedListener(user)
                            },
                            checkable = checkable,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    is PagingModel.Error -> ItemError(error = item.reason) {
                        listener.onRefresh()
                    }

                    is PagingModel.Loading -> ItemProgress()
                }
            }
        }

        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun UserScreenPreview(state: UserUiState) {
    ComposeAppTheme {
        UserScreen(
            viewModel = UserViewModel(
                UserStore(
                    UserReducer(),
                    UserEffectHandler(
                        object : UserRepository {},
                        UserUiModelMapper(),
                    ),
                    initState = state,
                ),
                emptyList(),
            )
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UserScreenEmptyLoadingPreview() {
    UserScreenPreview(state = UserUiState(emptyList(), UserStatus.EmptyLoading))
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UserScreenEmptyErrorPreview() {
    UserScreenPreview(
        state = UserUiState(
            emptyList(),
            UserStatus.EmptyError(RuntimeException("Test"))
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UserScreenIdlePreview() {
    UserScreenPreview(
        state = UserUiState(
            List(10) {
                UserUiModel(
                    id = it + 1L,
                    login = "jgummera8",
                    name = "Adison Levin",
                )
            },
        )
    )
}
