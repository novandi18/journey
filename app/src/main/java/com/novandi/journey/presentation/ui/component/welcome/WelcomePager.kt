package com.novandi.journey.presentation.ui.component.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun WelcomePager(
    modifier: Modifier = Modifier,
    itemsCount: Int = 3,
    navigateToHome: () -> Unit,
    itemContent: @Composable (index: Int) -> Unit
) {
    val pagerState = rememberPagerState { 3 }
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState
        ) {
            itemContent(it)
        }

        DotsIndicator(
            modifier = Modifier
                .padding(bottom = 52.dp)
                .align(Alignment.BottomCenter),
            totalDots = itemsCount,
            selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
            dotSize = 6.dp
        )
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            visible = pagerState.currentPage == (itemsCount - 1)
        ) {
            IconButton(
                modifier = Modifier.padding(bottom = 32.dp, end = 32.dp),
                onClick = navigateToHome
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    tint = Light
                )
            }
        }
    }
}