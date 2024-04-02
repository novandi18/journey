package com.novandi.journey.presentation.ui.component.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Yellow60
import com.valentinilk.shimmer.shimmer

@Composable
fun ChatBotLoading() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.Top),
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = null,
            tint = Yellow60
        )
        Column(
            modifier = Modifier.fillMaxWidth(.8f).shimmer(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(DarkGray40)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(.4f)
                    .height(32.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(DarkGray40)
            )
        }
    }
}

@Preview
@Composable
private fun ChatBotLoadingPreview() {
    JourneyTheme {
        ChatBotLoading()
    }
}