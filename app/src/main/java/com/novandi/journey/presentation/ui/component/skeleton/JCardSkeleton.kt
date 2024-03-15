package com.novandi.journey.presentation.ui.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun JCardSkeleton(total: Int = 5) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shimmer(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(total) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(DarkGray40)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    JourneyTheme {
        JCardSkeleton()
    }
}