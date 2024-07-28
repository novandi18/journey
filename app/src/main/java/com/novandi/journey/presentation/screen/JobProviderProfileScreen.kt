package com.novandi.journey.presentation.screen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Groups2
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
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.dialog.JDialog
import com.novandi.journey.presentation.ui.component.dialog.JDialogImagePreview
import com.novandi.journey.presentation.ui.component.dialog.LoadingDialog
import com.novandi.journey.presentation.ui.component.skeleton.ProfileSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.journey.presentation.viewmodel.JobProviderProfileViewModel
import com.novandi.utility.image.bitmapToUri
import com.novandi.utility.image.compressImage
import com.novandi.utility.image.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@Composable
fun JobProviderProfileScreen(
    viewModel: JobProviderProfileViewModel = hiltViewModel(),
    navigateToStarted: () -> Unit,
    navigateToEdit: (ProfileJobProvider) -> Unit,
    navigateToEmail: (String) -> Unit,
    navigateToPassword: () -> Unit
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val profile by viewModel.profile.collectAsState()
    val profileData by viewModel.profileData.observeAsState()
    val logoResponse by viewModel.logoResponse.collectAsState()

    LaunchedEffect(Unit) {
        if (token != null && accountId != null) {
            viewModel.getProfile(token.toString(), accountId.toString())
        }
    }

    LaunchedEffect(profile is Resource.Loading) {
        when (profile) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.setOnProfileData(profile?.data!!)
                viewModel.setOnLoading(false)
            }
            is Resource.Error -> viewModel.setOnLoading(false)
            else -> viewModel.setOnLoading(false)
        }
    }

    LaunchedEffect(logoResponse is Resource.Loading) {
        when (logoResponse) {
            is Resource.Loading -> viewModel.setOnUploadLoading(true)
            is Resource.Success -> {
                viewModel.setOnLoading(true)
                viewModel.setOnLoading(true)
                viewModel.getProfile(token.toString(), accountId.toString())
                Toast.makeText(context, logoResponse?.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnOpenDialogImagePreview(false)
                viewModel.setOnUploadLoading(false)
            }
            is Resource.Error -> {
                Toast.makeText(context, logoResponse?.message, Toast.LENGTH_SHORT).show()
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
        } else if (profileData == null) {
            NetworkError {
                viewModel.setOnLoading(true)
                viewModel.getProfile(token.toString(), accountId.toString())
            }
        } else {
            JobProviderProfileContent(
                data = profileData!!,
                logout = {
                    viewModel.logout()
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToStarted()
                    }, 500)
                },
                navigateToEmail = { email -> navigateToEmail(email) },
                navigateToPassword = navigateToPassword,
                openDialogImagePreview = viewModel.openDialogImagePreview,
                setOnOpenDialogImagePreview = viewModel::setOnOpenDialogImagePreview,
                uploadLogo = { logo ->
                    val imageFile = uriToFile(logo, context)
                    val requestImageFile = imageFile.asRequestBody("image/png".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "logo",
                        imageFile.name,
                        requestImageFile
                    )
                    viewModel.updateLogo(accountId.toString(), multipartBody)
                },
                uploadLoading = viewModel.uploadLoading
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp),
                onClick = {
                    navigateToEdit(profileData!!)
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
fun JobProviderProfileContent(
    data: ProfileJobProvider,
    logout: () -> Unit,
    navigateToEmail: (String) -> Unit,
    navigateToPassword: () -> Unit,
    openDialogImagePreview: Boolean,
    setOnOpenDialogImagePreview: (Boolean) -> Unit,
    uploadLogo: (Uri) -> Unit,
    uploadLoading: Boolean
) {
    val context = LocalContext.current
    val imageCropper = rememberImageCropper()
    val cropState = imageCropper.cropState
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val loadingDialog = remember { mutableStateOf(false) }

    var logo: Bitmap? by remember { mutableStateOf(null) }
    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            loadingDialog.value = true
            scope.launch {
                when (val result = imageCropper.crop(uri, context)) {
                    is CropResult.Cancelled -> { }
                    is CropError -> { }
                    is CropResult.Success -> {
                        val imageResult = result.bitmap.asAndroidBitmap()
                        val imageCompressed = imageResult.compressImage(context)
                        logo = imageCompressed
                        loadingDialog.value = false
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
                image = logo,
                onDismissRequest = {
                    setOnOpenDialogImagePreview(false)
                },
                onConfirmation = {
                    uploadLogo(bitmapToUri(logo!!))
                },
                uploadLoading = uploadLoading
            )
        }
        loadingDialog.value -> LoadingDialog()
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
                        .border(8.dp, Light, CircleShape)
                        .background(Light, CircleShape),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data.logo)
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
                    text = data.name,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Light
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.sectorName,
                    textAlign = TextAlign.Center,
                    color = Light,
                    fontSize = 16.sp
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .align(alignment = Alignment.CenterVertically),
                        imageVector = Icons.Filled.Mail,
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
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = DarkGray80,
                            fontSize = 14.sp
                        )
                    }
                }
                IconButton(
                    onClick = { navigateToEmail(data.email) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = stringResource(id = R.string.title_update_email),
                        tint = DarkGray80
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
                    imageVector = Icons.Filled.LocationCity,
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = DarkGray80,
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
                    imageVector = Icons.Rounded.Groups2,
                    contentDescription = stringResource(id = R.string.total_employee),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.total_employee),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = data.employees.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = DarkGray80,
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
            onClick = { openDialog.value = true },
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