package com.novandi.journey.presentation.ui.component.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun VacancySkeleton(
    paddingValues: PaddingValues = PaddingValues(24.dp)
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .shimmer(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(.6f)
                        .background(DarkGray40)
                )
                Spacer(
                    modifier = Modifier
                        .size(width = 60.dp, height = 28.dp)
                        .background(color = DarkGray40, shape = CircleShape)
                )
            }
            Spacer(modifier = Modifier
                .fillMaxWidth(.9f)
                .height(12.dp)
                .background(DarkGray40)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(DarkGray40)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth(.7f)
                .height(12.dp)
                .background(DarkGray40)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth(.5f)
                .height(12.dp)
                .background(DarkGray40)
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shimmer(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = DarkGray40, shape = CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(.6f)
                        .background(DarkGray40)
                )
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(.4f)
                        .background(DarkGray40)
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shimmer(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = DarkGray40, shape = CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(.3f)
                        .background(DarkGray40)
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(.7f)
                        .background(DarkGray40)
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shimmer(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = DarkGray40, shape = CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth(.35f)
                        .background(color = DarkGray40)
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(.6f)
                        .background(color = DarkGray40, shape = CircleShape)
                )
                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(.8f)
                        .background(color = DarkGray40, shape = CircleShape)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        VacancySkeleton()
    }
}