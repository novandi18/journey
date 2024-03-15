package com.novandi.journey.presentation.screen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.dialog.JDialog
import com.novandi.journey.presentation.ui.component.skeleton.ProfileSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.journey.presentation.viewmodel.JobProviderProfileViewModel

@Composable
fun JobProviderProfileScreen(
    viewModel: JobProviderProfileViewModel = hiltViewModel(),
    navigateToStarted: () -> Unit,
    navigateToEdit: (ProfileJobProvider) -> Unit,
    navigateToEmail: (String) -> Unit,
    navigateToPassword: () -> Unit
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
            JobProviderProfileContent(
                data = viewModel.profileData!!,
                logout = {
                    viewModel.logout()
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToStarted()
                    }, 500)
                },
                navigateToEmail = { email -> navigateToEmail(email) },
                navigateToPassword = navigateToPassword
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
fun JobProviderProfileContent(
    data: ProfileJobProvider,
    logout: () -> Unit,
    navigateToEmail: (String) -> Unit,
    navigateToPassword: () -> Unit
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
                    .background(color = Light, shape = CircleShape)
                    .padding(30.dp)
                    .size(100.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.logo)
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
                    text = data.name,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Light
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = data.sectorName,
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
                            color = DarkGray80
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
                        color = DarkGray80
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
                        color = DarkGray80
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
                    color = Green
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
                    color = Red
                )
            }
        }
    }
}