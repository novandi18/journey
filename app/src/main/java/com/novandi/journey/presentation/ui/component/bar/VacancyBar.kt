package com.novandi.journey.presentation.ui.component.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.core.domain.model.JobApplyStatus
import com.novandi.core.domain.model.Vacancy
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.utility.field.ApplicantStatus

@Composable
fun VacancyBar(
    vacancy: Vacancy,
    vacancyStatus: JobApplyStatus?,
    loading: Boolean,
    doApply: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = stringResource(id = R.string.apply_before),
                fontSize = 12.sp,
                color = Dark.copy(.6f)
            )
            Text(
                text = vacancy.deadlineTime,
                fontWeight = FontWeight.Medium,
                color = Red,
                fontSize = 14.sp
            )
        }
        if (vacancyStatus != null) {
            Text(
                modifier = Modifier
                    .background(
                        color = when (vacancyStatus.status) {
                            ApplicantStatus.ACCEPTED.value -> Green
                            ApplicantStatus.REJECTED.value -> Red
                            else -> DarkGray80
                        }, shape = CircleShape
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = when (vacancyStatus.status) {
                    ApplicantStatus.ACCEPTED.value -> stringResource(id = R.string.accepted)
                    ApplicantStatus.REJECTED.value -> stringResource(id = R.string.not_accepted)
                    else -> stringResource(id = R.string.pending)
                },
                color = Light
            )
        } else {
            Button(
                onClick = { doApply() },
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue40,
                    disabledContainerColor = Blue80
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.apply),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Light
                    )
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Light
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(id = R.string.apply),
                            tint = Light
                        )
                    }
                }
            }
        }
    }
}