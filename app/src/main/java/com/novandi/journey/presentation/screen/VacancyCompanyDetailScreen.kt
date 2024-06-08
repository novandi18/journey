package com.novandi.journey.presentation.screen

import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.rounded.Groups2
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.novandi.core.data.response.Resource
import com.novandi.core.domain.model.ProfileJobProvider
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.skeleton.ProfileSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.VacancyCompanyDetailViewModel

@Composable
fun VacancyCompanyDetailScreen(
    viewModel: VacancyCompanyDetailViewModel = hiltViewModel(),
    companyId: String,
    back: () -> Unit
) {
    val context = LocalContext.current
    val profile by viewModel.profile.collectAsState()
    val profileData by viewModel.profileData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProfile(companyId)
    }

    LaunchedEffect(profile is Resource.Loading) {
        when (profile) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.setOnProfileData(profile?.data!!)
                viewModel.setOnLoading(false)
                viewModel.resetState()
            }
            is Resource.Error -> {
                viewModel.setOnLoading(false)
                viewModel.resetState()
                Toast.makeText(context, profile?.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
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
                viewModel.getProfile(companyId)
            }
        } else {
            JobProviderProfileContent(data = profileData!!)

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
        }
    }
}

@Composable
fun JobProviderProfileContent(
    data: ProfileJobProvider
) {
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
                    .padding(horizontal = 24.dp, vertical = 16.dp),
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
                    imageVector = Icons.Rounded.LocationCity,
                    contentDescription = stringResource(id = R.string.province),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.province),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = data.province,
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
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = stringResource(id = R.string.city),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.city),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = data.city,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = DarkGray80,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

