package com.novandi.journey.presentation.ui.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun VacancyBarSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.shimmer(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth(.3f)
                    .background(DarkGray40)
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(.4f)
                    .background(DarkGray40)
            )
        }
        Spacer(
            modifier = Modifier
                .width(120.dp)
                .height(40.dp)
                .shimmer()
                .background(color = DarkGray40, shape = CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        VacancyBarSkeleton()
    }
}