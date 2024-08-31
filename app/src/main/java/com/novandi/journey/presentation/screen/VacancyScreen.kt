package com.novandi.journey.presentation.screen

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.MessagingRequest
import com.novandi.core.domain.model.File
import com.novandi.core.domain.model.VacancyDetailUser
import com.novandi.journey.BuildConfig
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.bar.VacancyBar
import com.novandi.journey.presentation.ui.component.dialog.FileDownloadDialog
import com.novandi.journey.presentation.ui.component.skeleton.VacancyBarSkeleton
import com.novandi.journey.presentation.ui.component.skeleton.VacancySkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.VacancyViewModel
import com.novandi.utility.consts.WorkerConsts
import com.novandi.utility.data.getFilenameFromUrl
import com.novandi.utility.field.ApplicantStatus
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun VacancyScreen(
    viewModel: VacancyViewModel = hiltViewModel(),
    vacancyId: String,
    navigateBack: () -> Unit,
    navigateToCompanyDetail: (vacancyId: String) -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val vacancy by viewModel.vacancy.collectAsState()
    val applyStatus by viewModel.applyResult.collectAsState()
    val cv by viewModel.cv.collectAsState()
    val notification by viewModel.notification.collectAsState()

    LaunchedEffect(accountId) {
        if (accountId != null) viewModel.getVacancy(vacancyId, accountId.toString())
    }

    LaunchedEffect(vacancy) {
        when (vacancy) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.setOnVacancyData(vacancy?.data)
                if (vacancy?.data != null) {
                    if (vacancy?.data?.userCv != null) {
                        viewModel.setOnCvFile(
                            File(
                                id = Random.nextInt().toString(),
                                name = getFilenameFromUrl(vacancy?.data?.userCv!!),
                                type = "PDF",
                                url = vacancy?.data?.userCv!!,
                                downloadedUri = null
                            )
                        )
                    }
                }
                viewModel.resetVacancyState()
                viewModel.setOnLoading(false)
            }
            is Resource.Error -> {
                Toast.makeText(context, vacancy?.message, Toast.LENGTH_SHORT).show()
                viewModel.resetVacancyState()
                viewModel.setOnLoading(false)
            }
            else -> {}
        }
    }

    LaunchedEffect(applyStatus is Resource.Loading) {
        when (applyStatus) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.setOnUpdateStatusApply(ApplicantStatus.PENDING.value)
                if (viewModel.vacancyData != null) {
                    viewModel.sendNotification(
                        request = MessagingRequest(
                            userId = viewModel.vacancyData!!.companyId,
                            messageTitle = context.getString(
                                R.string.notification_title_apply,
                                viewModel.vacancyData!!.position
                            ),
                            messageBody = context.getString(
                                R.string.notification_body_apply,
                                viewModel.vacancyData!!.position
                            )
                        )
                    )
                }
                viewModel.setOnApplyLoading(false)
                viewModel.resetApplyResultState()
            }
            is Resource.Error -> {
                Toast.makeText(context, applyStatus?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnApplyLoading(false)
                viewModel.resetApplyResultState()
            }
            else -> viewModel.setOnApplyLoading(false)
        }
    }

    LaunchedEffect(cv is Resource.Loading) {
        when (cv) {
            is Resource.Loading -> viewModel.setOnUploadCvLoading(true)
            is Resource.Success -> {
                if (cv?.data?.cv != null) {
                    viewModel.updateCvOnVacancyData(cv?.data?.cv)
                    viewModel.setOnCvFile(
                        File(
                            id = accountId!!,
                            name = getFilenameFromUrl(cv?.data?.cv!!),
                            type = "PDF",
                            url = cv?.data?.cv!!,
                        )
                    )
                    Toast.makeText(context, cv?.data?.message, Toast.LENGTH_SHORT).show()
                    viewModel.setOnUploadCvLoading(false)
                    viewModel.resetCvState()
                }
            }
            is Resource.Error -> {
                Toast.makeText(context, cv?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnUploadCvLoading(false)
                viewModel.resetCvState()
            }
            else -> {
                viewModel.setOnUploadCvLoading(false)
            }
        }
    }

    LaunchedEffect(notification is Resource.Loading) {
        when (notification) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.resetNotificationState()
            }
            is Resource.Error -> {
                Log.d("Notification", "Error: ${notification?.message}")
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.detail_vacancy),
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue40,
                    titleContentColor = Light
                ),
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Light
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Light)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        bottom = if (viewModel.cvDownloadShowing) 256.dp else 156.dp
                    )
            ) {
                if (viewModel.loading) {
                    VacancySkeleton()
                } else if (viewModel.vacancyData != null) {
                    VacancyContent(
                        vacancy = viewModel.vacancyData!!,
                        navigateToCompanyDetail = navigateToCompanyDetail
                    )
                } else {
                    NetworkError {
                        viewModel.getVacancy(vacancyId, accountId.toString())
                    }
                }
            }

            viewModel.downloadedCv.collectAsState().value.let { downloadedCv ->
                if (downloadedCv != null) {
                    val workInfo = downloadedCv.observeAsState().value
                    if (workInfo != null) {
                        when (workInfo.state) {
                            WorkInfo.State.SUCCEEDED -> {
                                val uri = workInfo.outputData.getString(WorkerConsts.KEY_FILE_URI) ?: ""
                                viewModel.setOnUpdateFile(
                                    isDownloading = false,
                                    downloadedUri = uri
                                )
                            }
                            WorkInfo.State.FAILED -> {
                                viewModel.setOnUpdateFile(
                                    isDownloading = false,
                                    downloadedUri = ""
                                )
                            }
                            WorkInfo.State.RUNNING -> {
                                viewModel.setOnUpdateFile(
                                    isDownloading = true
                                )
                            }
                            else -> {
                                viewModel.setOnUpdateFile(
                                    isDownloading = false,
                                    downloadedUri = ""
                                )
                            }
                        }
                    }
                }
            }

            val pdfDownloadPermissionLauncher = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) { permission ->
                val isGranted = permission.values.all { it }

                if (isGranted) {
                    viewModel.setOnCvDownloadShowing(true)
                    viewModel.downloadCv(context)
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.file_picker_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Column {
                    if (viewModel.cvFile != null) {
                        AnimatedVisibility(
                            modifier = Modifier.padding(16.dp),
                            visible = viewModel.cvDownloadShowing,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            FileDownloadDialog(
                                file = viewModel.cvFile!!,
                                close = {
                                    viewModel.setOnCvDownloadShowing(false)
                                    viewModel.resetDownloadedCvState()
                                },
                                openFile = { file ->
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    val uriData = FileProvider.getUriForFile(
                                        context,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        file.downloadedUri!!.toUri().toFile()
                                    )
                                    intent.setDataAndType(
                                        uriData,
                                        "application/pdf"
                                    )
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    viewModel.resetDownloadedCvState()
                                    viewModel.setOnCvDownloadShowing(false)
                                    try {
                                        val activity = context as Activity
                                        activity.startActivity(intent)
                                    } catch (e: ActivityNotFoundException) {
                                        Toast.makeText(
                                            context,
                                            "Can't open Pdf",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = DarkGray80
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Light)
                            .padding(16.dp)
                    ) {
                        if (viewModel.loading) {
                            VacancyBarSkeleton()
                        } else if (
                            viewModel.vacancyData != null
                        ) {
                            VacancyBar(
                                vacancy = viewModel.vacancyData!!,
                                loading = viewModel.applyLoading,
                                doApply = {
                                    viewModel.setOnApplyLoading(true)
                                    viewModel.applyVacancy(token.toString(), accountId.toString(), vacancyId)
                                },
                                cvFile = viewModel.cvFile,
                                updateCv = { cv ->
                                    viewModel.updateCv(accountId.toString(), cv)
                                },
                                uploadCvLoading = viewModel.uploadCvLoading,
                                downloadCv = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        viewModel.setOnCvDownloadShowing(true)
                                        viewModel.downloadCv(context)
                                    } else {
                                        pdfDownloadPermissionLauncher.launchMultiplePermissionRequest()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VacancyContent(
    vacancy: VacancyDetailUser,
    navigateToCompanyDetail: (vacancyId: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = vacancy.position,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
                Text(
                    modifier = Modifier
                        .background(color = Dark.copy(alpha = .1f), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = vacancy.jobType,
                    fontSize = 14.sp
                )
            }
            Text(
                text = vacancy.description,
                fontSize = 14.sp
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navigateToCompanyDetail(vacancy.companyId)
                }
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                model = vacancy.companyLogo,
                contentDescription = null
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = vacancy.companyName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = vacancy.companySector,
                    fontSize = 14.sp
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Filled.AssistWalker,
                contentDescription = stringResource(id = R.string.disability),
                tint = Blue40
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.vacancy_apply),
                    fontSize = 12.sp,
                    color = Dark.copy(.8f)
                )
                Text(
                    text = vacancy.disabilityName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = stringResource(id = R.string.disability),
                tint = Blue40
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.skill_requiremnt),
                    fontSize = 12.sp,
                    color = Dark.copy(.8f)
                )
                Text(
                    modifier = Modifier
                        .background(color = Dark.copy(.1f), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = vacancy.skillOne,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier
                        .background(color = Dark.copy(.1f), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = vacancy.skillTwo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        VacancyScreen(
            vacancyId = "1",
            navigateBack = {},
            navigateToCompanyDetail = {}
        )
    }
}