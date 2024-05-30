package com.novandi.journey.presentation.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.AssistantChat
import com.novandi.feature.assistant.VoiceToTextParser
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
    val context = LocalContext.current
    val voiceToTextParser = remember {
        VoiceToTextParser(context)
    }

    var record by remember {
        mutableStateOf(false)
    }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        record = isGranted
    }

    val voiceState by voiceToTextParser.state.collectAsStateWithLifecycle()

    LaunchedEffect(recordAudioLauncher) {
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

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
                    AssistantChat(
                        userMessage = results!!.data!!.user,
                        message = results!!.data!!.bot,
                        isFromMe = false
                    )
                )
                viewModel.setOnLoading(false)
                viewModel.resetResults()
            }
            is Resource.Error -> {
                viewModel.addChat(
                    AssistantChat(
                        message = results!!.message.toString(),
                        isFromMe = false,
                        userMessage = "",
                        isError = true
                    )
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

    LaunchedEffect(voiceState.spokenText) {
        if (voiceState.spokenText.isNotEmpty()) {
            viewModel.addChat(
                AssistantChat(
                    message = voiceState.spokenText,
                    isFromMe = true
                )
            )
            viewModel.ask(voiceState.spokenText)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.journey_assistant),
                        fontSize = 18.sp
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = if (chats[index].isFromMe) Alignment.CenterEnd else
                            Alignment.CenterStart
                    ) {
                        ChatItem(
                            chat = chats[index],
                            reload = {
                                viewModel.ask(chats[index].userMessage!!)
                                viewModel.deleteChat(chats[index].id!!)
                            },
                            isLast = index == chats.size - 1
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
                },
                voiceState = voiceState,
                voiceParser = voiceToTextParser
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