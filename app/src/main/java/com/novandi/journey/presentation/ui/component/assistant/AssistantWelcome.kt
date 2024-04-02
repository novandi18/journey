package com.novandi.journey.presentation.ui.component.assistant

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Yellow40
import com.novandi.journey.presentation.ui.theme.Yellow60
import com.novandi.journey.presentation.ui.theme.Yellow80

@Composable
fun AssistantWelcome() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .border(1.dp, Yellow60, RoundedCornerShape(16.dp)),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Yellow40,
            contentColor = Yellow80
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = Yellow80
            )
            Text(
                text = stringResource(id = R.string.assistant_welcome),
                fontSize = 12.sp,
                color = Yellow80
            )
        }
    }
}