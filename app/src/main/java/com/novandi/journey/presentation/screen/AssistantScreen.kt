package com.novandi.journey.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.AssistantChat
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.assistant.AssistantSheet
import com.novandi.journey.presentation.ui.component.assistant.AssistantWelcome
import com.novandi.journey.presentation.ui.component.assistant.ChatBotLoading
import com.novandi.journey.presentation.ui.component.assistant.ChatBox
import com.novandi.journey.presentation.ui.component.assistant.ChatItem
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.AssistantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(
    viewModel: AssistantViewModel = hiltViewModel()
) {
    val results by viewModel.results.collectAsState()
    val chats by viewModel.chats.observeAsState(emptyList())
    val listState = rememberLazyListState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(chats) {
        if (chats.isNotEmpty()) {
            listState.animateScrollToItem(chats.size - 1)
        }
    }

    LaunchedEffect(results is Resource.Loading) {
        when (results) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                viewModel.addChat(
                    AssistantChat(results!!.data!!.bot, false)
                )
                viewModel.setOnLoading(false)
                viewModel.resetResults()
            }
            is Resource.Error -> {
                viewModel.addChat(
                    AssistantChat(results!!.message.toString(), false)
                )
                viewModel.setOnLoading(false)
                viewModel.resetResults()
            }
            else -> viewModel.resetResults()
        }
    }

    if (showBottomSheet) {
        AssistantSheet(
            deleteChats = {
                viewModel.deleteAll()
            },
            showBottomSheet = { showBottomSheet = it }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.journey_assistant)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue40,
                    titleContentColor = Light
                ),
                actions = {
                    IconButton(onClick = {
                        showBottomSheet = true
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = null,
                            tint = Light
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Light)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
                state = listState
            ) {
                item {
                    AssistantWelcome()
                }

                items(chats.size) { index ->
                    var columnSize by remember { mutableStateOf(Size.Zero) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { layoutCoordinates ->
                                columnSize = layoutCoordinates.size.toSize()
                            },
                        contentAlignment = if (chats[index].isFromMe) Alignment.CenterEnd else
                            Alignment.CenterStart
                    ) {
                        val maxWidth = LocalDensity.current.run { (0.8f * columnSize.width).toDp() }

                        ChatItem(
                            modifier = Modifier.widthIn(0.dp, maxWidth),
                            text = chats[index].message,
                            isFromMe = chats[index].isFromMe
                        )
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = viewModel.loading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        ChatBotLoading()
                    }
                }
            }

            ChatBox(
                modifier = Modifier.align(Alignment.BottomCenter),
                value = viewModel.prompt,
                setValue = viewModel::setOnPrompt,
                addToChats = { chat ->
                    viewModel.addChat(chat)
                },
                ask = { prompt ->
                    viewModel.ask(prompt)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        AssistantScreen()
    }
}