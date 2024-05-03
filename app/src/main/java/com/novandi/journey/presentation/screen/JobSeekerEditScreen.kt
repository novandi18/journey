package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.MapsHomeWork
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.AssistWalker
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobSeekerEditRequest
import com.novandi.core.domain.model.ProfileJobSeeker
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.field.JDropdownDialog
import com.novandi.journey.presentation.ui.component.field.JTextField
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobSeekerEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerEditScreen(
    viewModel: JobSeekerEditViewModel = hiltViewModel(),
    profile: ProfileJobSeeker?,
    back: () -> Unit
) {
    val context = LocalContext.current
    val gender = listOf(stringResource(id = R.string.man), stringResource(id = R.string.woman))
    val token by viewModel.token.observeAsState()
    val accountId by viewModel.accountId.observeAsState()
    val response by viewModel.response.collectAsState()

    LaunchedEffect(Unit) {
        if (profile != null) {
            viewModel.setOnFullName(profile.fullName)
            viewModel.setOnPhoneNumber(profile.phoneNumber)
            viewModel.setOnAddress(profile.address)
            viewModel.setOnAge(profile.age)
            viewModel.setOnGender(gender.indexOf(profile.gender))
            viewModel.setOnDisability(viewModel.disabilities.map { it.disability }.indexOf(profile.disabilityName))
            viewModel.setOnSkillOne(viewModel.skills.map { it.skill }.indexOf(profile.skillOne))
            viewModel.setOnSkillTwo(viewModel.skills.map { it.skill }.indexOf(profile.skillTwo))
        }
    }

    LaunchedEffect(response is Resource.Loading) {
        when (response) {
            is Resource.Loading -> viewModel.setOnLoading(true)
            is Resource.Success -> {
                Toast.makeText(context, response?.data?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnLoading(false)
                viewModel.setDisabilityDataStore(viewModel.disabilities[viewModel.disability].en)
                viewModel.setSkillOneDataStore(viewModel.skills[viewModel.skillOne].skill)
                viewModel.setSkillTwoDataStore(viewModel.skills[viewModel.skillTwo].skill)
                back()
            }
            is Resource.Error -> {
                Toast.makeText(context, response?.message, Toast.LENGTH_SHORT).show()
                viewModel.setOnLoading(false)
            }
            else -> viewModel.setOnLoading(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_update_profile),
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue40,
                    navigationIconContentColor = Light,
                    titleContentColor = Light
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Light)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                JTextField(
                    leadingIcon = Icons.Filled.Person,
                    label = stringResource(id = R.string.fullname),
                    keyboardType = KeyboardType.Text,
                    placeholder = stringResource(id = R.string.fullname_placeholder),
                    onKeyUp = viewModel::setOnFullName,
                    textValue = viewModel.fullName,
                    isReadOnly = viewModel.loading
                )
                JTextField(
                    leadingIcon = Icons.Filled.Phone,
                    label = stringResource(id = R.string.phone_number),
                    keyboardType = KeyboardType.Phone,
                    placeholder = stringResource(id = R.string.phonenumber_placeholder),
                    onKeyUp = viewModel::setOnPhoneNumber,
                    textValue = viewModel.phoneNumber,
                    isReadOnly = viewModel.loading
                )
                JTextField(
                    leadingIcon = Icons.Filled.MapsHomeWork,
                    label = stringResource(id = R.string.address),
                    keyboardType = KeyboardType.Text,
                    placeholder = stringResource(id = R.string.address_placeholder),
                    onKeyUp = viewModel::setOnAddress,
                    textValue = viewModel.address,
                    isReadOnly = viewModel.loading
                )
                JTextField(
                    leadingIcon = Icons.Filled.Person3,
                    label = stringResource(id = R.string.age),
                    placeholder = stringResource(id = R.string.age_placeholder),
                    keyboardType = KeyboardType.Number,
                    onKeyUp = viewModel::setOnAge,
                    textValue = viewModel.age,
                    isReadOnly = viewModel.loading
                )
                JDropdownDialog(
                    icon = Icons.Rounded.Man,
                    label = stringResource(id = R.string.gender_placeholder),
                    items = gender,
                    selectedIndex = viewModel.gender,
                    setSelectedItem = viewModel::setOnGender
                )
                JDropdownDialog(
                    icon = Icons.Rounded.AssistWalker,
                    label = stringResource(id = R.string.disability_placeholder),
                    items = viewModel.disabilities.map { it.disability },
                    selectedIndex = viewModel.disability,
                    setSelectedItem = viewModel::setOnDisability
                )
                JDropdownDialog(
                    icon = Icons.AutoMirrored.Filled.StarHalf,
                    label = stringResource(id = R.string.first_skills),
                    items = viewModel.skills.map { it.skill },
                    selectedIndex = viewModel.skillOne,
                    setSelectedItem = viewModel::setOnSkillOne
                )
                JDropdownDialog(
                    icon = Icons.AutoMirrored.Filled.StarHalf,
                    label = stringResource(id = R.string.second_skills),
                    items = viewModel.skills.map { it.skill },
                    selectedIndex = viewModel.skillTwo,
                    setSelectedItem = viewModel::setOnSkillTwo
                )

                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Blue40,
                        contentColor = Light,
                        disabledContainerColor = Blue80,
                        disabledContentColor = Light
                    ),
                    enabled = !viewModel.loading,
                    onClick = {
                        if (viewModel.validateFields()) {
                            val request = JobSeekerEditRequest(
                                fullName = viewModel.fullName,
                                disabilityId = viewModel.disabilities[viewModel.disability].id,
                                address = viewModel.address,
                                gender = gender[viewModel.gender],
                                age = viewModel.age.toInt(),
                                phoneNumber = viewModel.phoneNumber,
                                skillOne = viewModel.skills[viewModel.skillOne].id,
                                skillTwo = viewModel.skills[viewModel.skillTwo].id
                            )
                            viewModel.update(token.toString(), accountId.toString(), request)
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.fields_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.submit),
                            color = Light,
                            fontSize = 16.sp
                        )
                        AnimatedVisibility(viewModel.loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = Light
                            )
                        }
                    }
                }
            }
        }
    }
}