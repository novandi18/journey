package com.novandi.journey.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.File
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.journey.BuildConfig
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.dialog.FileDownloadDialog
import com.novandi.journey.presentation.ui.component.skeleton.ProfileSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobProviderApplicantProfileViewModel
import com.novandi.utility.consts.WorkerConsts
import com.novandi.utility.data.getFilenameFromUrl
import kotlin.random.Random

@Composable
fun JobProviderApplicantProfileScreen(
    viewModel: JobProviderApplicantProfileViewModel = hiltViewModel(),
    applicantId: String,
    back: () -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val profile by viewModel.profile.collectAsState()

    LaunchedEffect(token != null, accountId != null) {
        viewModel.getApplicant(token.toString(), accountId.toString(), applicantId)
    }

    LaunchedEffect(profile is Resource.Loading) {
        when (profile) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                viewModel.setOnProfileData(profile?.data)
                if (profile?.data != null) {
                    viewModel.setOnCvFile(
                        File(
                            id = Random.nextInt().toString(),
                            name = getFilenameFromUrl(profile?.data?.cv!!),
                            type = "PDF",
                            url = profile?.data?.cv!!,
                            downloadedUri = null
                        )
                    )
                }
                viewModel.setOnLoading(false)
                viewModel.resetState()
            }
            is Resource.Error -> {
                viewModel.setOnLoading(false)
                viewModel.resetState()
            }
            else -> viewModel.setOnLoading(false)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Light)
    ) {
        if (viewModel.loading) {
            ProfileSkeleton()
        } else if (viewModel.profileData == null) {
            NetworkError {
                viewModel.getApplicant(token.toString(), accountId.toString(), applicantId)
            }
        } else {
            Content(
                data = viewModel.profileData!!
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp, start = 6.dp),
                onClick = back
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = Light
                )
            }

            if (viewModel.cvFile != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    AnimatedVisibility(
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
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("NewApi")
@Composable
private fun Content(
    viewModel: JobProviderApplicantProfileViewModel = hiltViewModel(),
    data: ProfileJobSeeker
) {
    val context = LocalContext.current
    val downloadedCv by viewModel.downloadedCv.collectAsState()
    if (downloadedCv != null) {
        val workInfo = downloadedCv!!.observeAsState().value
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Blue40)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .border(8.dp, Light, CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.profilePhotoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = data.fullName,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Light
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(alignment = Alignment.CenterVertically),
                    imageVector = Icons.Filled.Email,
                    contentDescription = stringResource(id = R.string.email),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.email),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = data.email,
                        fontSize = 14.sp
                    )
                }
            }

            HorizontalDivider(
                color = Dark.copy(alpha = .1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(alignment = Alignment.CenterVertically),
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(id = R.string.address),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.address),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = data.address,
                        fontSize = 14.sp
                    )
                }
            }

            HorizontalDivider(
                color = Dark.copy(alpha = .1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(alignment = Alignment.CenterVertically),
                    imageVector = Icons.Filled.Phone,
                    contentDescription = stringResource(id = R.string.phone_number),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.phone_number),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = data.phoneNumber,
                        fontSize = 14.sp
                    )
                }
            }

            HorizontalDivider(
                color = Dark.copy(alpha = .1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = if (data.cv != null) Modifier.weight(.75f) else Modifier,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .align(alignment = Alignment.CenterVertically),
                        imageVector = Icons.Rounded.Description,
                        contentDescription = stringResource(id = R.string.cv),
                        tint = DarkGray80
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cv),
                            fontSize = 12.sp,
                            color = DarkGray80
                        )
                        Text(
                            text = if (data.cv != null) getFilenameFromUrl(data.cv!!) else
                                stringResource(id = R.string.cv_empty),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = DarkGray80,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Row(
                    modifier = if (data.cv != null) Modifier.weight(.2f) else Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if (data.cv != null) {
                        IconButton(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    viewModel.setOnCvDownloadShowing(true)
                                    viewModel.downloadCv(context)
                                } else {
                                    pdfDownloadPermissionLauncher.launchMultiplePermissionRequest()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Download,
                                contentDescription = stringResource(id = R.string.download_cv),
                                tint = DarkGray80
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                color = Dark.copy(alpha = .1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(alignment = Alignment.CenterVertically),
                    imageVector = Icons.Filled.AssistWalker,
                    contentDescription = stringResource(id = R.string.disability),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.disability),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = data.disabilityName,
                        fontSize = 14.sp
                    )
                }
            }

            HorizontalDivider(
                color = Dark.copy(alpha = .1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(alignment = Alignment.CenterVertically),
                    imageVector = Icons.AutoMirrored.Filled.StarHalf,
                    contentDescription = stringResource(id = R.string.skill_applicant),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.skill_applicant),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .background(color = Blue40, shape = CircleShape)
                                .padding(horizontal = 12.dp, vertical = 2.dp),
                            text = data.skillOne,
                            maxLines = 1,
                            color = Light,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp
                        )
                        Text(
                            modifier = Modifier
                                .background(color = Blue40, shape = CircleShape)
                                .padding(horizontal = 12.dp, vertical = 2.dp),
                            text = data.skillTwo,
                            maxLines = 1,
                            color = Light,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}