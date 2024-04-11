package com.novandi.journey.presentation.screen

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Password
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.journey.R
import com.novandi.journey.presentation.service.NotificationService
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
import com.novandi.utility.image.bitmapToUri
import com.novandi.utility.image.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
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

    LaunchedEffect(Unit) {
        viewModel.getProfile(token.toString(), accountId.toString())
    }

    LaunchedEffect(profile is Resource.Loading) {
        when (profile) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                viewModel.setOnProfileData(profile?.data)
                viewModel.setOnLoading(false)
            }
            is Resource.Error -> viewModel.setOnLoading(false)
            else -> viewModel.setOnLoading(false)
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
            }
            is Resource.Error -> {
                Toast.makeText(context, photoProfile?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnOpenDialogImagePreview(false)
                viewModel.setOnUploadLoading(false)
            }
            else -> {
                viewModel.setOnOpenDialogImagePreview(false)
                viewModel.setOnUploadLoading(false)
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
                data = viewModel.profileData!!,
                logout = {
                    viewModel.logout()
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToStarted()
                    }, 500)
                },
                uploadLoading = viewModel.uploadLoading,
                navigateToEmail = navigateToEmail,
                navigateToPassword = navigateToPassword,
                openDialogImagePreview = viewModel.openDialogImagePreview,
                setOnOpenDialogImagePreview = viewModel::setOnOpenDialogImagePreview,
                uploadPhoto = { photo ->
                    val imageFile = uriToFile(photo, context)
                    val requestImageFile = imageFile.asRequestBody("image/png".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "profile_photo_url",
                        imageFile.name,
                        requestImageFile
                    )
                    viewModel.updatePhotoProfile(accountId.toString(), multipartBody)
                }
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
        }
    }
}

@Composable
fun JobSeekerProfileContent(
    data: ProfileJobSeeker,
    logout: () -> Unit,
    navigateToEmail: (String) -> Unit,
    navigateToPassword: () -> Unit,
    openDialogImagePreview: Boolean,
    setOnOpenDialogImagePreview: (Boolean) -> Unit,
    uploadPhoto: (Uri) -> Unit,
    uploadLoading: Boolean
) {
    val context = LocalContext.current
    val imageCropper = rememberImageCropper()
    val cropState = imageCropper.cropState
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }

    var photo: Bitmap? by remember { mutableStateOf(null) }
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
                        setOnOpenDialogImagePreview(true)
                    }
                }
            }
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
        openDialogImagePreview -> {
            JDialogImagePreview(
                image = photo,
                onDismissRequest = {
                    setOnOpenDialogImagePreview(false)
                },
                onConfirmation = {
                    uploadPhoto(bitmapToUri(photo!!))
                },
                uploadLoading = uploadLoading
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
        }
    }
}