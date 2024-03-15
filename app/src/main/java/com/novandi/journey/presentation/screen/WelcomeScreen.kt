package com.novandi.journey.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novandi.journey.R
import com.novandi.journey.presentation.common.WelcomeItem
import com.novandi.journey.presentation.ui.component.welcome.WelcomePager
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.viewmodel.WelcomeViewModel

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = hiltViewModel(),
    navigateToHome: () -> Unit
) {
    val welcomes = listOf(
        WelcomeItem(
            stringResource(id = R.string.onboarding_title_1),
            stringResource(id = R.string.onboarding_description_1),
            R.drawable.img_welcome_1
        ),
        WelcomeItem(
            stringResource(id = R.string.onboarding_title_2),
            stringResource(id = R.string.onboarding_description_2),
            R.drawable.img_welcome_2
        ),
        WelcomeItem(
            stringResource(id = R.string.onboarding_title_3),
            stringResource(id = R.string.onboarding_description_3),
            R.drawable.img_welcome_3
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue40)
    ) {
        WelcomePager(
            itemsCount = welcomes.size,
            navigateToHome = {
                viewModel.setWelcomed(true)
                navigateToHome()
            },
            itemContent = { index ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
                ) {
                    Image(
                        modifier = Modifier.size(240.dp),
                        painter = painterResource(id = welcomes[index].image),
                        contentDescription = welcomes[index].description
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = welcomes[index].title,
                            color = Light,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )

                        Text(
                            text = welcomes[index].description,
                            color = Light,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomePreview() {
    JourneyTheme {
        WelcomeScreen(navigateToHome = {})
    }
}