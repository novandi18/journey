package com.novandi.journey.presentation.ui.component.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.core.consts.JobTypes
import com.novandi.core.domain.model.Vacancy
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red

@Composable
fun JCardVacancy(
    vacancy: Vacancy,
    navigateToDetail: (String) -> Unit,
    simple: Boolean = false
) {
    var showMore by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Light),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    navigateToDetail(vacancy.id)
                }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = vacancy.placementAddress,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = JobTypes.types()[vacancy.jobType - 1],
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
                    text = vacancy.skillOne,
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
                    text = vacancy.skillTwo,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (!simple) {
            HorizontalDivider()
            if (vacancy.description.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .clickable {
                            showMore = !showMore
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.description_placeholder),
                            fontSize = 12.sp,
                            color = DarkGray80
                        )
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = if (showMore) Icons.Rounded.ExpandLess else
                                Icons.Rounded.ExpandMore,
                            contentDescription = null,
                            tint = DarkGray80
                        )
                    }
                    AnimatedVisibility(showMore) {
                        Text(
                            text = vacancy.description,
                            fontSize = 14.sp
                        )
                    }
                }
                HorizontalDivider()
            }
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.disability),
                    fontSize = 12.sp,
                    color = DarkGray80
                )
                Text(
                    text = vacancy.disabilityName
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.closed_at),
                fontSize = 10.sp,
                color = DarkGray80
            )
            Text(
                text = vacancy.deadlineTime,
                fontWeight = FontWeight.Medium,
                color = Red
            )
        }
    }
}