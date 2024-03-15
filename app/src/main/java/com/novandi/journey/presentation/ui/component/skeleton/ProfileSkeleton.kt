package com.novandi.journey.presentation.ui.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun ProfileSkeleton() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Blue40)
                .padding(24.dp)
                .shimmer(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .size(150.dp)
                    .background(color = Blue80, shape = CircleShape)
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth(.8f)
                    .background(color = Blue80, shape = RoundedCornerShape(size = 8.dp))
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth(.5f)
                    .background(color = Blue80, shape = RoundedCornerShape(size = 8.dp))
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }

        HorizontalDivider(
            color = Dark.copy(alpha = .1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shimmer(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            repeat(3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(42.dp)
                            .align(alignment = Alignment.CenterVertically)
                            .background(color = DarkGray40, shape = CircleShape)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                                .fillMaxWidth(.4f)
                                .background(color = DarkGray40, shape = RoundedCornerShape(size = 4.dp))
                        )
                        Spacer(
                            modifier = Modifier
                                .height(28.dp)
                                .fillMaxWidth()
                                .background(color = DarkGray40, shape = RoundedCornerShape(size = 8.dp))
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            color = Dark.copy(alpha = .1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shimmer(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .size(38.dp)
                        .background(color = DarkGray40, shape = CircleShape)
                )
                Spacer(
                    modifier = Modifier
                        .height(38.dp)
                        .fillMaxWidth()
                        .background(color = DarkGray40, shape = RoundedCornerShape(size = 8.dp))
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    JourneyTheme {
        ProfileSkeleton()
    }
}