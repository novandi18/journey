package com.novandi.journey.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.core.domain.model.JobSeeker
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.auth.AuthTitle
import com.novandi.journey.presentation.ui.component.field.JDropdownField
import com.novandi.journey.presentation.ui.component.field.JPasswordField
import com.novandi.journey.presentation.ui.component.field.JTextField
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobSeekerRegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerRegisterScreen(
    viewModel: JobSeekerRegisterViewModel = hiltViewModel(),
    backToLogin: () -> Unit,
    navigateToSkill: (JobSeeker) -> Unit
) {
    var topBarShadow by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if (scrollState.isScrollInProgress) {
        topBarShadow = scrollState.value >= 100
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(
                    if (topBarShadow) 6.dp else 0.dp
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.jobseeker),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Light,
                    titleContentColor = Dark,
                    navigationIconContentColor = Dark
                ),
                navigationIcon = {
                    IconButton(onClick = { backToLogin() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .height(28.dp)
                            .width(28.dp)
                            .background(color = Blue40, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Light)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            JobSeekerRegisterContent(
                viewModel = viewModel,
                continueRegister = { data ->
                    navigateToSkill(data)
                }
            )
        }
    }
}

@Composable
fun JobSeekerRegisterContent(
    viewModel: JobSeekerRegisterViewModel,
    continueRegister: (JobSeeker) -> Unit
) {
    val genders = listOf(stringResource(id = R.string.man), stringResource(id = R.string.woman))
    val disabilities = viewModel.disabilities.map { it.disability }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AuthTitle(
            title = stringResource(id = R.string.register_title),
            description = stringResource(id = R.string.register_description)
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            JTextField(
                leadingIcon = Icons.Filled.Person,
                label = stringResource(id = R.string.fullname),
                keyboardType = KeyboardType.Text,
                placeholder = stringResource(id = R.string.fullname_placeholder),
                onKeyUp = viewModel::setOnName,
                textValue = viewModel.name
            )
            JTextField(
                leadingIcon = Icons.Filled.Email,
                label = stringResource(id = R.string.email),
                keyboardType = KeyboardType.Email,
                placeholder = stringResource(id = R.string.email_placeholder),
                onKeyUp = viewModel::setOnEmail,
                textValue = viewModel.email
            )
            JTextField(
                leadingIcon = Icons.Filled.Phone,
                label = stringResource(id = R.string.phone_number),
                keyboardType = KeyboardType.Phone,
                placeholder = stringResource(id = R.string.phonenumber_placeholder),
                onKeyUp = viewModel::setOnPhoneNumber,
                textValue = viewModel.phoneNumber
            )
            JTextField(
                leadingIcon = Icons.Filled.Home,
                label = stringResource(id = R.string.address),
                keyboardType = KeyboardType.Text,
                placeholder = stringResource(id = R.string.address_placeholder),
                onKeyUp = viewModel::setOnAddress,
                textValue = viewModel.address
            )
            JTextField(
                leadingIcon = Icons.Filled.Person3,
                label = stringResource(id = R.string.age),
                placeholder = stringResource(id = R.string.age_placeholder),
                keyboardType = KeyboardType.Number,
                onKeyUp = viewModel::setOnAge,
                textValue = viewModel.age
            )
            JDropdownField(
                icon = Icons.Filled.Man,
                label = stringResource(id = R.string.gender_placeholder),
                data = genders,
                itemSelected = viewModel.gender,
                setItemSelected = viewModel::setOnGender
            )
            JDropdownField(
                icon = Icons.Filled.AssistWalker,
                label = stringResource(id = R.string.disability_placeholder),
                data = disabilities,
                itemSelected = viewModel.disability,
                setItemSelected = viewModel::setOnDisability
            )
            JPasswordField(
                onKeyUp = viewModel::setOnPassword,
                textValue = viewModel.password
            )
            Box(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 56.dp, top = 24.dp)
            ) {
                Button(
                    onClick = {
                        continueRegister(
                            JobSeeker(
                                fullName = viewModel.name,
                                email = viewModel.email,
                                phoneNumber = viewModel.phoneNumber,
                                address = viewModel.address,
                                age = viewModel.age.toInt(),
                                disabilityId = viewModel.disabilities[viewModel.disability].id,
                                password = viewModel.password,
                                gender = genders[viewModel.gender]
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = viewModel.validateFields(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Blue40,
                        contentColor = Light,
                        disabledContainerColor = Blue80,
                        disabledContentColor = Light
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.continue_register),
                        color = Light,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        JobSeekerRegisterScreen(
            backToLogin = {},
            navigateToSkill = {}
        )
    }
}