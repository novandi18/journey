package com.novandi.journey.presentation.ui.component.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Yellow60

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    text: String,
    isFromMe: Boolean
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = 48f,
                    topEnd = 48f,
                    bottomStart = if (isFromMe) 48f else 0f,
                    bottomEnd = if (isFromMe) 0f else 48f
                )
            )
            .background(if (isFromMe) Blue40 else Light)
            .padding(if (isFromMe) 16.dp else 0.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!isFromMe) {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.Top),
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = Yellow60
                )
            }
            Text(
                text = text,
                color = if (isFromMe) Light else Dark,
                fontSize = 16.sp
            )
        }
    }
}