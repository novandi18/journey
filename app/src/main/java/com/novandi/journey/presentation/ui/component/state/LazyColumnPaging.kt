package com.novandi.journey.presentation.ui.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton

@Composable
fun <T: Any> LazyColumnPaging(
    items: LazyPagingItems<T>,
    content: @Composable (T) -> Unit,
    lazyListState: LazyListState = rememberLazyListState()
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
                    item {
                        JCardSkeleton(3)
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    item {
                        LoadStateError(errorMessage = stringResource(id = R.string.network_error)) {
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
                    item {
                        LoadStateError(errorMessage = stringResource(id = R.string.network_error)) {
                            retry()
                        }
                    }
                }
            }
        }
    }
}