package com.novandi.journey.presentation.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.novandi.core.consts.Skills
import com.novandi.core.data.response.Resource
import com.novandi.core.data.source.remote.request.JobSeekerRegisterRequest
import com.novandi.core.domain.model.JobSeeker
import com.novandi.core.domain.model.Skill
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.JobSeekerSkillViewModel

@Composable
fun JobSeekerSkillScreen(
    viewModel: JobSeekerSkillViewModel = hiltViewModel(),
    dataFromRegister: JobSeeker?,
    backToLogin: () -> Unit
) {
    val register by viewModel.register.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(register is Resource.Loading) {
        when (register) {
            is Resource.Loading -> viewModel.setIsLoading(true)
            is Resource.Success -> {
                viewModel.setIsLoading(false)
                Toast.makeText(context, register?.data?.message, Toast.LENGTH_SHORT).show()
                backToLogin()
            }
            is Resource.Error -> {
                viewModel.setIsLoading(false)
                Toast.makeText(context, register?.message.toString(), Toast.LENGTH_SHORT).show()
            }
            else -> viewModel.setIsLoading(false)
        }
    }

    JobSeekerSkillContent(
        viewModel = viewModel,
        skills = Skills.getSkills(),
        registerNow = {
            val request = JobSeekerRegisterRequest(
                fullName = dataFromRegister!!.fullName,
                email = dataFromRegister.email,
                password = dataFromRegister.password,
                skillOne = viewModel.skillOne,
                skillTwo = viewModel.skillTwo,
                disabilityId = dataFromRegister.disabilityId,
                address = dataFromRegister.address,
                gender = dataFromRegister.gender,
                age = dataFromRegister.age,
                phoneNumber = dataFromRegister.phoneNumber
            )
            viewModel.register(request)
        }
    )
}

@Composable
fun JobSeekerSkillContent(
    viewModel: JobSeekerSkillViewModel,
    skills: List<Skill>,
    registerNow: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Blue40, Blue80)
                    )
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            if (skills.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box {
                        CircularProgressIndicator(
                            color = Light,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            } else {
                Box {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 128.dp, top = 100.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp,
                    ) {
                        items(skills.size) { index ->
                            TextButton(
                                modifier = Modifier
                                    .background(
                                        color = Light.copy(
                                            alpha = if (viewModel.skillOne == skills[index].id || viewModel.skillTwo == skills[index].id)
                                                .8f else .1f
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 8.dp),
                                onClick = {
                                    if (viewModel.skillOne == skills[index].id) {
                                        viewModel.setOnSkillOne(-1)
                                    } else if (viewModel.skillTwo == skills[index].id) {
                                        viewModel.setOnSkillTwo(-1)
                                    } else {
                                        if (viewModel.skillOne == -1) {
                                            viewModel.setOnSkillOne(skills[index].id)
                                        } else if (viewModel.skillTwo == -1){
                                            viewModel.setOnSkillTwo(skills[index].id)
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = skills[index].skill,
                                    color = if (viewModel.skillOne == skills[index].id || viewModel.skillTwo == skills[index].id)
                                        Blue80 else Light
                                )
                            }
                        }
                    }
                }
            }
            Text(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Blue40, Color.Transparent)
                        )
                    )
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 38.dp, bottom = 16.dp),
                text = stringResource(id = R.string.choose_skills),
                fontSize = 28.sp,
                color = Light,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }

        AnimatedVisibility(visible = viewModel.skillOne != -1 && viewModel.skillTwo != -1) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Blue80)
                        )
                    )
                    .height(148.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .height(64.dp),
                    onClick = {
                        registerNow()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Light,
                        contentColor = Blue40,
                        disabledContainerColor = DarkGray40,
                        disabledContentColor = Blue40
                    ),
                    enabled = !viewModel.isLoading
                ) {
                    AnimatedVisibility(viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 8.dp),
                            color = Blue80
                        )
                    }
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = stringResource(id = R.string.btn_register),
                        fontSize = 20.sp,
                        color = Blue80
                    )
                }
            }
        }
    }
}