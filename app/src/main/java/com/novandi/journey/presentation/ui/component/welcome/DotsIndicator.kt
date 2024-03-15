package com.novandi.journey.presentation.ui.component.welcome

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Light,
    unSelectedColor: Color = Light.copy(.5f),
    dotSize: Dp = 6.dp
) {

    LazyRow(
        modifier = modifier.wrapContentSize()
    ) {
        items(totalDots) { index ->
            Dots(
                color = if (selectedIndex == index) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != 4) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}