package com.novandi.journey.presentation.ui.component.card

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.novandi.core.domain.model.Applicant
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.field.JTextAreaField
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Green80
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.journey.presentation.ui.theme.Red80

@Composable
fun JCardApplicant(
    applicant: Applicant,
    onAccept: (isAccept: Boolean, notes: String?) -> Unit,
    loading: List<Pair<String, Boolean>>,
    status: Boolean? = null,
    navigateToApplicantProfile: (applicantId: String) -> Unit
) {
    val context = LocalContext.current
    var showMore by rememberSaveable {
        mutableStateOf(false)
    }
    var notes by rememberSaveable {
        mutableStateOf("")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Light),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    navigateToApplicantProfile(applicant.id)
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape),
                model = applicant.profilePhotoUrl,
                contentDescription = applicant.fullName
            )
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = applicant.fullName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Dark
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = applicant.email,
                    fontSize = 12.sp,
                    color = DarkGray80
                )
            }
        }

        Column(
            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.skill_applicant),
                    fontSize = 12.sp,
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
                        text = applicant.skillOne,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier
                            .background(color = Blue40, shape = RoundedCornerShape(8.dp))
                            .padding(vertical = 2.dp, horizontal = 8.dp),
                        text = applicant.skillTwo,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column {
                Text(
                    text = stringResource(id = R.string.disability),
                    fontSize = 12.sp,
                    color = DarkGray80
                )
                Text(
                    text = applicant.disabilityName,
                    fontSize = 14.sp,
                    color = Dark
                )
            }
            Column {
                Text(
                    text = stringResource(id = R.string.applied_at),
                    fontSize = 12.sp,
                    color = DarkGray80
                )
                Text(
                    text = applicant.appliedAt,
                    fontSize = 14.sp,
                    color = Dark
                )
            }
        }

        HorizontalDivider(
            color = DarkGray
        )

        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showMore = !showMore
            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Light
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RectangleShape
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 8.dp),
                imageVector = if (showMore) Icons.Rounded.KeyboardArrowUp else
                    Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = Dark
            )
        }

        AnimatedVisibility(visible = showMore) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            ) {
                if (status == null) {
                    JTextAreaField(
                        title = stringResource(id = R.string.notes_for_job_seeker),
                        onKeyUp = { value -> notes = value },
                        value = notes,
                        placeholder = stringResource(id = R.string.notes_placeholder),
                        isReadOnly = loading.contains(Pair(applicant.id, true)) ||
                                loading.contains(Pair(applicant.id, false))
                    )
                    Text(
                        text = stringResource(id = R.string.notes_job_seeker_alert),
                        fontSize = 12.sp,
                        color = Red
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.notes_for_job_seeker),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = if (applicant.notes != null) applicant.notes!! else
                            stringResource(id = R.string.no_notes),
                        fontSize = 14.sp,
                        color = Dark
                    )
                }
            }
        }

        HorizontalDivider(
            color = DarkGray
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.End)
        ) {
            if (status != null) {
                Text(
                    modifier = Modifier
                        .background(
                            color = if (status) Green else Red,
                            shape = CircleShape
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    text = stringResource(
                        id = if (status) R.string.accepted else R.string.not_accepted
                    ),
                    color = Light
                )
            } else {
                Button(
                    onClick = { onAccept(false, null) },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Red,
                        contentColor = Light,
                        disabledContainerColor = Red80,
                        disabledContentColor = Light
                    ),
                    enabled = !loading.any { it.first == applicant.id }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            visible = loading.contains(Pair(applicant.id, false))
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Light,
                                strokeWidth = 2.dp
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.reject),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Button(
                    onClick = {
                        if (notes.trim().isEmpty()) {
                            if (!showMore) showMore = true
                            Toast.makeText(
                                context,
                                context.getString(R.string.notes_required),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onAccept(true, notes)
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Green,
                        contentColor = Light,
                        disabledContainerColor = Green80,
                        disabledContentColor = Light
                    ),
                    enabled = !loading.any { it.first == applicant.id }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            visible = loading.contains(Pair(applicant.id, true))
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Light,
                                strokeWidth = 2.dp
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.accept),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}