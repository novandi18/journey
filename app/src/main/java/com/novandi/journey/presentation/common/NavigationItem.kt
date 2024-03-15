package com.novandi.journey.presentation.common

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.RawValue

data class NavigationItem(
    val title: Int,
    val icon: @RawValue ImageVector,
    val screen: @RawValue Screen,
    val contentDescription: Int
)