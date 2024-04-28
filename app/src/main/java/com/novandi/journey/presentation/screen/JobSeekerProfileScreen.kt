package com.novandi.journey.presentation.screen

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asAndroidBitmap
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
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.File
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.journey.BuildConfig
import com.novandi.journey.R
import com.novandi.journey.presentation.service.NotificationService
import com.novandi.journey.presentation.ui.component.dialog.FileDownloadDialog
import com.novandi.journey.presentation.ui.component.dialog.JDialog
import com.novandi.journey.presentation.ui.component.dialog.JDialogImagePreview
import com.novandi.journey.presentation.ui.component.skeleton.ProfileSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.journey.presentation.viewmodel.JobSeekerProfileViewModel
import com.novandi.utility.consts.NetworkUrls
import com.novandi.utility.consts.WorkerConsts
import com.novandi.utility.data.convertUriToPdf
import com.novandi.utility.image.bitmapToUri
import com.novandi.utility.image.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@Composable
fun JobSeekerProfileScreen(
    viewModel: JobSeekerProfileViewModel = hiltViewModel(),
    navigateToStarted: () -> Unit,
    navigateToEdit: (ProfileJobSeeker) -> Unit,
    navigateToEmail: (String) -> Unit,
    navigateToPassword: () -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val profile by viewModel.profile.collectAsState()
    val photoProfile by viewModel.photoProfile.collectAsState()
    val cv by viewModel.cv.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProfile(token.toString(), accountId.toString())
    }

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

    LaunchedEffect(profile is Resource.Loading) {
        when (profile) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                viewModel.setOnProfileData(profile?.data)
                if (profile?.data?.cv != null) {
                    viewModel.setOnCvFile(
                        File(
                            id = profile?.data!!.id,
                            name = profile?.data?.cv!!,
                            type = "PDF",
                            url = "${NetworkUrls.JOURNEY}users/cv/${viewModel.profileData!!.cv!!}",
                            downloadedUri = null
                        )
                    )
                }
                viewModel.setOnLoading(false)
            }
            is Resource.Error -> viewModel.setOnLoading(false)
            else -> {}
        }
    }

    LaunchedEffect(photoProfile is Resource.Loading) {
        when (photoProfile) {
            is Resource.Loading -> viewModel.setOnUploadLoading(true)
            is Resource.Success -> {
                viewModel.getProfile(token.toString(), accountId.toString())
                Toast.makeText(context, photoProfile?.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnOpenDialogImagePreview(false)
                viewModel.setOnUploadLoading(false)
                viewModel.resetPhotoProfileState()
            }
            is Resource.Error -> {
                Toast.makeText(context, photoProfile?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnOpenDialogImagePreview(false)
                viewModel.setOnUploadLoading(false)
                viewModel.resetPhotoProfileState()
            }
            else -> {
                viewModel.setOnOpenDialogImagePreview(false)
                viewModel.setOnUploadLoading(false)
            }
        }
    }

    LaunchedEffect(cv is Resource.Loading) {
        when (cv) {
            is Resource.Loading -> viewModel.setOnUploadCvLoading(true)
            is Resource.Success -> {
                viewModel.updateCvOnProfileData(cv?.data?.cv)
                Toast.makeText(context, cv?.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnUploadCvLoading(false)
                viewModel.resetCvState()
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Light)
    ) {
        if (viewModel.loading) {
            ProfileSkeleton()
        } else if (viewModel.profileData == null) {
            NetworkError {
                viewModel.getProfile(token.toString(), accountId.toString())
            }
        } else {
            JobSeekerProfileContent(
                viewModel = viewModel,
                data = viewModel.profileData!!,
                logout = {
                    viewModel.logout()
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToStarted()
                    }, 500)
                },
                navigateToEmail = navigateToEmail,
                navigateToPassword = navigateToPassword,
                accountId = accountId.toString()
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp),
                onClick = {
                    navigateToEdit(viewModel.profileData!!)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = stringResource(id = R.string.title_update_profile),
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

@Composable
fun JobSeekerProfileContent(
    viewModel: JobSeekerProfileViewModel,
    data: ProfileJobSeeker,
    logout: () -> Unit,
    navigateToEmail: (String) -> Unit,
    navigateToPassword: () -> Unit,
    accountId: String
) {
    val context = LocalContext.current
    val imageCropper = rememberImageCropper()
    val cropState = imageCropper.cropState
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val openCvDialog = remember { mutableStateOf(false) }

    var photo: Bitmap? by remember { mutableStateOf(null) }
    val selectedPdfUri = remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                when (val result = imageCropper.crop(uri, context)) {
                    is CropResult.Cancelled -> { }
                    is CropError -> { }
                    is CropResult.Success -> {
                        photo = result.bitmap.asAndroidBitmap()
                        viewModel.setOnOpenDialogImagePreview(true)
                    }
                }
            }
        }
    }

    val pdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedPdfUri.value = uri
            openCvDialog.value = true
        }
    }

    val pdfPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pdfLauncher.launch(arrayOf("application/pdf"))
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.file_picker_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val pdfDownloadPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        var isGranted = false
        it.forEach { _, b ->
            isGranted = b
        }

        if (isGranted) {
            viewModel.setOnCvDownloadShowing(true)
            viewModel.downloadCv(
                viewModel.cvFile!!
            )
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.file_picker_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if(cropState != null) ImageCropperDialog(state = cropState)

    when {
        openDialog.value -> {
            JDialog(
                onDismissRequest = { openDialog.value = false },
                onConfirmation = {
                    openDialog.value = false
                    val service = Intent(context, NotificationService::class.java)
                    context.stopService(service)
                    logout()
                },
                dialogTitle = stringResource(id = R.string.logout),
                dialogText = stringResource(id = R.string.logout_desc),
                confirmText = stringResource(id = R.string.logout_confirm),
                icon = Icons.AutoMirrored.Filled.Logout
            )
        }
        viewModel.openDialogImagePreview -> {
            JDialogImagePreview(
                image = photo,
                onDismissRequest = {
                    viewModel.setOnOpenDialogImagePreview(false)
                },
                onConfirmation = {
                    val imageFile = uriToFile(bitmapToUri(photo!!), context)
                    val requestImageFile = imageFile.asRequestBody("image/png".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "profile_photo_url",
                        imageFile.name,
                        requestImageFile
                    )
                    viewModel.updatePhotoProfile(accountId, multipartBody)
                },
                uploadLoading = viewModel.uploadLoading
            )
        }
        openCvDialog.value -> {
            JDialog(
                onDismissRequest = {
                    selectedPdfUri.value = null
                    openCvDialog.value = false
                },
                onConfirmation = {
                    val cvFile = convertUriToPdf(context, selectedPdfUri.value!!)
                    val requestCvFile =
                        cvFile?.asRequestBody("application/pdf".toMediaTypeOrNull())
                    if (requestCvFile != null) {
                        val multipartBody = MultipartBody.Part.createFormData(
                            "cv",
                            cvFile.name,
                            requestCvFile
                        )
                        viewModel.updateCv(accountId, multipartBody)
                    }
                    openCvDialog.value = false
                },
                dialogTitle = stringResource(id = R.string.cv),
                dialogText = stringResource(id = R.string.cv_desc),
                confirmText = stringResource(id = R.string.cv_confirm),
                icon = Icons.Rounded.Description
            )
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
            Box(
                contentAlignment = Alignment.Center
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
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .border(width = 6.dp, color = Blue40, shape = CircleShape),
                    onClick = {
                        imageLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Light,
                        contentColor = DarkGray80
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Image,
                        contentDescription = null
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.fullName,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Light
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.email,
                    textAlign = TextAlign.Center,
                    color = Light,
                    fontSize = 16.sp
                )
            }
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
                    contentDescription = stringResource(id = R.string.your_skills),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.your_skills),
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
                            text = if (data.cv != null) data.cv!! else
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
                    modifier = if (data.cv != null) Modifier.weight(.25f) else Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if (viewModel.uploadCvLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(42.dp)
                                .padding(end = 12.dp, top = 4.dp),
                            color = DarkGray80
                        )
                    } else {
                        if (data.cv != null) {
                            IconButton(
                                onClick = {
                                    pdfDownloadPermissionLauncher.launch(arrayOf(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Download,
                                    contentDescription = stringResource(id = R.string.download_cv),
                                    tint = DarkGray80
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                pdfPermissionLauncher.launch(
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = stringResource(id = R.string.update_cv),
                                tint = DarkGray80
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                color = Dark.copy(alpha = .4f)
            )

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navigateToEmail(data.email) },
                shape = RectangleShape
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(alignment = Alignment.CenterVertically),
                        imageVector = Icons.Rounded.Email,
                        contentDescription = stringResource(id = R.string.title_update_email),
                        tint = Green
                    )
                    Text(
                        text = stringResource(id = R.string.title_update_email),
                        color = Green,
                        fontSize = 14.sp
                    )
                }
            }

            HorizontalDivider(
                color = Dark.copy(alpha = .1f)
            )

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = navigateToPassword,
                shape = RectangleShape
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(alignment = Alignment.CenterVertically),
                        imageVector = Icons.Rounded.Password,
                        contentDescription = stringResource(id = R.string.title_update_password),
                        tint = Green
                    )
                    Text(
                        text = stringResource(id = R.string.title_update_password),
                        color = Green,
                        fontSize = 14.sp
                    )
                }
            }
            HorizontalDivider(
                color = Dark.copy(alpha = .4f)
            )

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    openDialog.value = true
                },
                shape = RectangleShape
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(alignment = Alignment.CenterVertically),
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(id = R.string.logout),
                        tint = Red
                    )
                    Text(
                        text = stringResource(id = R.string.logout),
                        color = Red,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(
                    if (viewModel.cvDownloadShowing) 100.dp else 0.dp
                )
            )
        }
    }
}