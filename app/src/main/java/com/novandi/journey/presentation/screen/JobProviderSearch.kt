package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.card.JCard
import com.novandi.journey.presentation.ui.component.skeleton.JCardSkeleton
import com.novandi.journey.presentation.ui.component.state.NetworkError
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.PoppinsFont
import com.novandi.journey.presentation.viewmodel.JobProviderSearchViewModel

@Composable
fun JobProviderSearch(
    viewModel: JobProviderSearchViewModel = hiltViewModel(),
    companyId: String,
    back: () -> Unit,
    navigateToVacancy: (String) -> Unit,
) {
    val context = LocalContext.current
    val token by viewModel.token.observeAsState()
    val vacancies by viewModel.vacancies.observeAsState()

    LaunchedEffect(token != null) {
        if (token != null) {
            viewModel.getVacancies(token.toString(), companyId)
        }
    }

    LaunchedEffect(vacancies is Resource.Loading) {
        when (vacancies) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                if (vacancies!!.data != null) {
                    viewModel.setOnData(vacancies!!.data)
                    if (viewModel.data != null) viewModel.setOnFilteredData(viewModel.data!!)
                }
                viewModel.setOnLoading(false)
            }
            is Resource.Error -> {
                Toast.makeText(context, vacancies?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnLoading(false)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Light)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Blue40)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.weight(.1f),
                    onClick = back
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = Light
                    )
                }
                TextField(
                    modifier = Modifier
                        .weight(.95f)
                        .background(Color.Transparent),
                    value = viewModel.query,
                    onValueChange = { query ->
                        viewModel.setOnQuery(query)
                        if (viewModel.data != null) {
                            viewModel.setOnFilteredData(
                                viewModel.data!!.filter { vacancy ->
                                    vacancy.placementAddress.contains(query, ignoreCase = true)
                                }
                            )
                        }
                    },
                    maxLines = 1,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.search_vacancy_placeholder),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Blue40,
                        focusedContainerColor = Blue40,
                        unfocusedIndicatorColor = Blue40,
                        focusedIndicatorColor = Blue40,
                        unfocusedPlaceholderColor = Light.copy(.6f),
                        focusedPlaceholderColor = Light.copy(.6f),
                        cursorColor = Light
                    ),
                    textStyle = TextStyle(
                        color = Light,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFont.Poppins
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        if (viewModel.query.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    viewModel.setOnQuery("")
                                    viewModel.setOnFilteredData(viewModel.data!!)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null,
                                    tint = Light
                                )
                            }
                        }
                    }
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (viewModel.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    JCardSkeleton(3)
                }
            } else if (viewModel.data != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.filteredData.size) { index ->
                        JCard(viewModel.filteredData[index]) { vacancyId ->
                            navigateToVacancy(vacancyId)
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NetworkError {
                        viewModel.setOnLoading(true)
                        viewModel.getVacancies(token.toString(), companyId)
                    }
                }
            }
        }
    }
}