package com.novandi.journey.presentation.ui.component.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.core.domain.model.AssistantChat
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
    ask: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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
            OutlinedTextField(
                modifier = Modifier
                    .weight(.85f)
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
            IconButton(
                modifier = Modifier
                    .weight(.15f)
                    .height(56.dp),
                onClick = {
                    addToChats(AssistantChat(value, true))
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