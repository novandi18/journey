package com.novandi.journey.presentation.ui.component.dialog

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun JDialogImagePreview(
    image: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    uploadLoading: Boolean
) {
    val brightness = -80f
    val colorMatrix = floatArrayOf(
        1f, 0f, 0f, 0f, brightness,
        0f, 1f, 0f, 0f, brightness,
        0f, 0f, 1f, 0f, brightness,
        0f, 0f, 0f, 1f, 0f
    )

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Light
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.image_preview),
                    fontWeight = FontWeight.Bold,
                    color = Dark,
                    fontSize = 16.sp
                )
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (image != null) {
                        Image(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp, color = Dark, shape = CircleShape
                                ),
                            bitmap = image.asImageBitmap(),
                            contentDescription = stringResource(id = R.string.image_preview),
                            contentScale = ContentScale.Crop,
                            colorFilter = if (uploadLoading)
                                ColorFilter.colorMatrix(ColorMatrix(colorMatrix)) else null
                        )
                    }
                    if (uploadLoading) {
                        CircularProgressIndicator(
                            color = Light
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        },
                        enabled = !uploadLoading
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = DarkGray40,
                            fontSize = 16.sp
                        )
                    }
                    TextButton(
                        onClick = {
                            onConfirmation()
                        },
                        enabled = !uploadLoading
                    ) {
                        Text(
                            text = stringResource(id = R.string.upload),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}