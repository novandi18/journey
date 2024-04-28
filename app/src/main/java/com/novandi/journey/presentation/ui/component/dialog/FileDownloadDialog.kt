package com.novandi.journey.presentation.ui.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.core.domain.model.File
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red

@Composable
fun FileDownloadDialog(
    file: File,
    close: () -> Unit,
    openFile: (File) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Light
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(.8f)
                    .clickable {
                        if (file.downloadedUri != null && file.downloadedUri!!.isNotEmpty()) {
                            openFile(file)
                        }
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = file.name,
                    color = DarkGray80,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                Text(
                    text = stringResource(
                        id = if (file.isDownloading) {
                            R.string.downloading
                        } else if (file.downloadedUri != null) {
                            R.string.click_to_open_file
                        } else R.string.failed_download
                    ),
                    color = if (file.isDownloading) DarkGray80 else if (file.downloadedUri != null)
                        Blue40 else Red,
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(.2f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (file.isDownloading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(32.dp),
                        color = DarkGray80
                    )
                } else {
                    IconButton(
                        onClick = close
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp),
                            tint = DarkGray80,
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }
                }
            }
        }
    }
}