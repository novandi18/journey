package com.novandi.journey.presentation.ui.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.core.domain.model.JobApplyStatus
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.utility.field.ApplicantStatus

@Composable
fun JCardApply(
    data: JobApplyStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Light),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = data.vacancyPlacementAddress,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = data.companyName,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = DarkGray80
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .background(color = Blue40, shape = RoundedCornerShape(8.dp))
                        .padding(vertical = 2.dp, horizontal = 8.dp),
                    text = data.skillOne,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .background(color = Blue40, shape = RoundedCornerShape(8.dp))
                        .padding(vertical = 2.dp, horizontal = 8.dp),
                    text = data.skillTwo,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
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
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = stringResource(id = R.string.applied_at),
                fontSize = 12.sp,
                color = DarkGray80
            )
            Text(
                text = data.appliedAt
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = Modifier
                    .background(
                        color = when (data.status) {
                            ApplicantStatus.ACCEPTED.value -> Green
                            ApplicantStatus.REJECTED.value -> Red
                            else -> DarkGray80
                        }, shape = CircleShape
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = when (data.status) {
                    ApplicantStatus.ACCEPTED.value -> stringResource(id = R.string.accepted)
                    ApplicantStatus.REJECTED.value -> stringResource(id = R.string.not_accepted)
                    else -> stringResource(id = R.string.pending)
                },
                color = Light
            )
        }
    }
}