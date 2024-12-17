package com.novandi.journey.presentation.ui.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton

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
    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = {
            onRefresh()
        }
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

                    loadState.append is LoadState.Loading -> {
                        setIsRefreshing(true)
                        item {
                            JCardSkeleton(3)
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        setIsRefreshing(false)
                        item {
                            LoadStateError(
                                errorMessage = stringResource(id = R.string.network_error)
                            ) {
                                retry()
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        setIsRefreshing(false)
                        item {
                            LoadStateError(
                                errorMessage = stringResource(id = R.string.network_error)
                            ) {
                                retry()
                            }
                        }
                    }
                }
            }
        }
    }
}