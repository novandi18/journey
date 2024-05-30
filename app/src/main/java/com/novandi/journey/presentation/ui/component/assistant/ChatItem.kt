package com.novandi.journey.presentation.ui.component.assistant

import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.core.domain.model.AssistantChat
import com.novandi.feature.assistant.TextToSpeech
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Yellow60
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    chat: AssistantChat,
    reload: () -> Unit,
    isLast: Boolean,
    maxChatCharacters: Int = 100
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var mediaPlayer by remember {
        mutableStateOf<MediaPlayer?>(null)
    }
    var isPlaying by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var textExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .widthIn(min = 0.dp, max = 280.dp)
            .background(
                color = if (chat.isFromMe) Blue40 else Light,
                shape = RoundedCornerShape(
                    topStart = 48f,
                    topEnd = 48f,
                    bottomStart = if (chat.isFromMe) 48f else 0f,
                    bottomEnd = if (chat.isFromMe) 0f else 48f
                )
            )
            .padding(if (chat.isFromMe) 16.dp else 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!chat.isFromMe) {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.Top),
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = Yellow60
                )
            }
            if (chat.isFromMe) {
                Text(
                    text = chat.message,
                    color = Light,
                    fontSize = 16.sp
                )
            } else {
                val regex = Regex("\\*\\*(.*?)\\*\\*", RegexOption.DOT_MATCHES_ALL)
                val annotatedString = buildAnnotatedString {
                    var lastIndex = 0
                    regex.findAll(chat.message.trimIndent()).forEach { matchResult ->
                        val range = matchResult.range
                        append(chat.message.trimIndent().substring(lastIndex, range.first))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(matchResult.groupValues[1])
                        }
                        lastIndex = range.last + 1
                    }
                    if (lastIndex < chat.message.trimIndent().length) {
                        append(chat.message.trimIndent().substring(lastIndex))
                    }
                }
                val expandedText = if (annotatedString.length > maxChatCharacters) {
                    if (textExpanded) annotatedString.text else annotatedString.substring(0, 97) + "..."
                } else annotatedString.text
                Text(
                    text = expandedText,
                    color = Dark,
                    fontSize = 16.sp
                )
            }
        }
        if (!chat.isFromMe && !chat.isError) {
            Row(
                modifier = Modifier.padding(start = 28.dp),
            ) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(
                        modifier = Modifier.padding(horizontal = 0.dp),
                        onClick = {
                            coroutineScope.launch {
                                if (isPlaying) {
                                    mediaPlayer?.stop()
                                    isPlaying = false
                                } else {
                                    val audioTask = async {
                                        TextToSpeech(context = context)
                                            .synthesize(text = chat.message)
                                    }

                                    val audio = audioTask.await()
                                    val outputFile =
                                        File(
                                            context.getExternalFilesDir(null),
                                            "assistant_output.mp3"
                                        )
                                    val outputStream = FileOutputStream(outputFile)
                                    outputStream.write(audio)
                                    outputStream.close()

                                    mediaPlayer = MediaPlayer.create(
                                        context,
                                        Uri.fromFile(outputFile)
                                    )

                                    mediaPlayer?.setOnCompletionListener {
                                        isPlaying = false
                                        mediaPlayer?.stop()
                                    }

                                    mediaPlayer?.start()
                                    isPlaying = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = null,
                            tint = if (isPlaying) Green else DarkGray80
                        )
                    }
                }

                if (isLast) {
                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                        IconButton(
                            modifier = Modifier.padding(horizontal = 0.dp),
                            onClick = reload
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = null,
                                tint = DarkGray80
                            )
                        }
                    }
                }

                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(
                        modifier = Modifier.padding(horizontal = 0.dp),
                        onClick = {
                            clipboardManager.setText(AnnotatedString(chat.message))
                            Toast.makeText(
                                context,
                                context.getString(R.string.copied_to_clipboard),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = null,
                            tint = DarkGray80
                        )
                    }
                }

                if (!isLast) {
                    if (chat.message.length > maxChatCharacters) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            IconButton(
                                onClick = {
                                    textExpanded = !textExpanded
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.padding(horizontal = 0.dp),
                                    imageVector = if (textExpanded) Icons.Rounded.KeyboardArrowUp
                                        else Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = DarkGray80
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}