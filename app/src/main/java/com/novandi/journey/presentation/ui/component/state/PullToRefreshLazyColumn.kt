package com.novandi.journey.presentation.ui.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> PullToRefreshLazyColumn(
    paddingValues: PaddingValues = PaddingValues(16.dp),
    items: List<T>,
    content: @Composable (T) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val state = rememberPullToRefreshState()

    Box(
        modifier = Modifier.nestedScroll(state.nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) {
                content(it)
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