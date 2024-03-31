package com.novandi.journey.presentation.ui.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: Any> PullToRefreshPaging(
    items: LazyPagingItems<T>,
    content: @Composable (T) -> Unit,
    isRefreshing: Boolean,
    setIsRefreshing: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val state = rememberPullToRefreshState()

    Box(
        modifier = Modifier.nestedScroll(state.nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.itemCount) { index ->
                if (items[index] != null) {
                    content(items[index]!!)
                }
            }

            items.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        setIsRefreshing(true)
                        item {
                            JCardSkeleton(3)
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        setIsRefreshing(false)
                        val error = items.loadState.refresh as LoadState.Error
                        item {
                            LoadStateError(errorMessage = error.error.localizedMessage!!) {
                                retry()
                            }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            JCardSkeleton(3)
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = items.loadState.refresh as LoadState.Error
                        item {
                            LoadStateError(errorMessage = error.error.localizedMessage!!) {
                                retry()
                            }
                        }
                    }

                    loadState.refresh is LoadState.NotLoading -> {
                        setIsRefreshing(false)
                    }
                }
            }
        }

        if (state.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                state.startRefresh()
            } else {
                state.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Blue40,
            containerColor = Light
        )
    }
}