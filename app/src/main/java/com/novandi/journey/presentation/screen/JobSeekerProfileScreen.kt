package com.novandi.journey.presentation.screen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
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
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.dialog.JDialog
import com.novandi.journey.presentation.ui.component.skeleton.ProfileSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.journey.presentation.viewmodel.JobSeekerProfileViewModel

@Composable
fun JobSeekerProfileScreen(
    viewModel: JobSeekerProfileViewModel = hiltViewModel(),
    navigateToStarted: () -> Unit
) {
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val profile by viewModel.profile.collectAsState()

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

    Column(
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
                }
            )
        }
    }
}

@Composable
fun JobSeekerProfileContent(
    data: ProfileJobSeeker,
    logout: () -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }

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
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Blue40)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(150.dp)
                    .background(
                        brush = Brush.verticalGradient(listOf(Light, Light)),
                        shape = CircleShape
                    )
                    .align(alignment = Alignment.CenterHorizontally),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.profilePhotoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.profile_photo),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.fullName,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Light
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.email,
                    textAlign = TextAlign.Center,
                    color = Light
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
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
                        text = data.phoneNumber
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
                        text = data.disabilityName
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
        }

        HorizontalDivider(
            color = Dark.copy(alpha = .1f)
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
                    color = Red
                )
            }
        }
    }
}