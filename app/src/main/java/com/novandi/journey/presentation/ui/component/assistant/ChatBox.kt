package com.novandi.journey.presentation.ui.component.assistant

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.core.domain.model.AssistantChat
import com.novandi.feature.assistant.VoiceToTextParser
import com.novandi.feature.assistant.VoiceToTextParserState
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray
import com.novandi.journey.presentation.ui.theme.DarkGray60
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    value: String,
    setValue: (String) -> Unit,
    addToChats: (AssistantChat) -> Unit,
    ask: (String) -> Unit,
    voiceState: VoiceToTextParserState,
    voiceParser: VoiceToTextParser
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Box(
        modifier = modifier.background(Light)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = if (value.contains("\n"))
                Alignment.Bottom else Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedContent(
                modifier = Modifier.weight(.85f),
                targetState = voiceState.isSpeaking,
                label = "AnimatedIconButton"
            ) { isSpeaking ->
                if (isSpeaking) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkGray
                        ),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    voiceParser.stopListening()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = stringResource(id = R.string.cancel),
                                    tint = DarkGray80
                                )
                            }
                            Text(
                                text = "Mendengarkan...",
                                fontSize = 16.sp,
                                color = DarkGray80
                            )
                        }
                    }
                } else {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .focusRequester(focusRequester),
                        value = value,
                        onValueChange = setValue,
                        shape = if (value.contains("\n")) RoundedCornerShape(15.dp)
                        else CircleShape,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.ask_something),
                                color = DarkGray60,
                                fontSize = 16.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = DarkGray,
                            focusedContainerColor = DarkGray,
                            cursorColor = Dark,
                            focusedTextColor = Dark,
                            unfocusedTextColor = DarkGray80,
                            unfocusedBorderColor = DarkGray,
                            focusedBorderColor = DarkGray
                        ),
                        maxLines = 3,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    )
                }
            }
            if (value.isEmpty()) {
                IconButton(
                    modifier = Modifier
                        .weight(.15f)
                        .height(56.dp),
                    onClick = {
                        if (voiceState.isSpeaking) {
                            voiceParser.stopListening()

                            Handler(Looper.getMainLooper()).postDelayed({
                                if (voiceState.spokenText.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.speak_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, 2000)
                        } else {
                            voiceParser.startListening()
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Blue40
                    )
                ) {
                    AnimatedContent(
                        targetState = voiceState.isSpeaking,
                        label = "AnimatedIcon"
                    ) { isSpeaking ->
                        if (isSpeaking) {
                            Icon(
                                imageVector = Icons.Rounded.Done,
                                contentDescription = stringResource(id = R.string.stop),
                                tint = Light
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Mic,
                                contentDescription = stringResource(id = R.string.record),
                                tint = Light
                            )
                        }
                    }
                }
            } else {
                IconButton(
                    modifier = Modifier
                        .weight(.15f)
                        .height(56.dp),
                    onClick = {
                        addToChats(
                            AssistantChat(message = value, isFromMe = true)
                        )
                        ask(value)
                        focusManager.clearFocus()
                        setValue("")
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Blue40
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = null,
                        tint = Light
                    )
                }
            }
        }
    }
}