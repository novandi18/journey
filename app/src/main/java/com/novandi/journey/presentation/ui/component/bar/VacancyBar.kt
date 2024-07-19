package com.novandi.journey.presentation.ui.component.bar

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.novandi.core.domain.model.File
import com.novandi.core.domain.model.VacancyDetailUser
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.component.dialog.JDialog
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Blue80
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Green
import com.novandi.journey.presentation.ui.theme.Light
import com.novandi.journey.presentation.ui.theme.Red
import com.novandi.utility.data.convertUriToPdf
import com.novandi.utility.data.getFilenameFromUrl
import com.novandi.utility.field.ApplicantStatus
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VacancyBar(
    vacancy: VacancyDetailUser,
    loading: Boolean,
    doApply: () -> Unit,
    cvFile: File?,
    updateCv: (MultipartBody.Part) -> Unit,
    uploadCvLoading: Boolean,
    downloadCv: (File) -> Unit
) {
    val context = LocalContext.current
    val selectedPdfUri = remember { mutableStateOf<Uri?>(null) }
    val openCvDialog = remember { mutableStateOf(false) }
    val applyDialog = remember { mutableStateOf(false) }

    val pdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedPdfUri.value = uri
            openCvDialog.value = true
        }
    }

    val pdfPermissionLauncher = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    ) { isGranted ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            pdfLauncher.launch(arrayOf("application/pdf"))
        } else {
            if (isGranted) {
                pdfLauncher.launch(arrayOf("application/pdf"))
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.file_picker_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    when {
        openCvDialog.value -> {
            JDialog(
                onDismissRequest = {
                    selectedPdfUri.value = null
                    openCvDialog.value = false
                },
                onConfirmation = {
                    val cvFileReq = convertUriToPdf(context, selectedPdfUri.value!!)
                    val requestCvFile =
                        cvFileReq?.asRequestBody("application/pdf".toMediaTypeOrNull())
                    if (requestCvFile != null) {
                        val multipartBody = MultipartBody.Part.createFormData(
                            "cv",
                            cvFileReq.name,
                            requestCvFile
                        )
                        updateCv(multipartBody)
                    }
                    openCvDialog.value = false
                },
                dialogTitle = stringResource(id = R.string.cv),
                dialogText = stringResource(id = R.string.cv_desc),
                confirmText = stringResource(id = R.string.cv_confirm),
                icon = Icons.Rounded.Description
            )
        }
        applyDialog.value -> {
            JDialog(
                onDismissRequest = {
                    applyDialog.value = false
                },
                onConfirmation = {
                    doApply()
                    applyDialog.value = false
                },
                dialogTitle = stringResource(id = R.string.apply_title),
                dialogText = stringResource(id = R.string.apply_desc),
                confirmText = stringResource(id = R.string.apply_confirm),
                icon = Icons.Rounded.Work,
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = if (vacancy.userCv != null) Modifier.weight(.75f) else Modifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(alignment = Alignment.CenterVertically),
                    imageVector = Icons.Rounded.Description,
                    contentDescription = stringResource(id = R.string.cv),
                    tint = DarkGray80
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.cv),
                        fontSize = 12.sp,
                        color = DarkGray80
                    )
                    Text(
                        text = if (vacancy.userCv != null) getFilenameFromUrl(vacancy.userCv!!) else
                            stringResource(id = R.string.cv_empty),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = DarkGray80,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Row(
                modifier = if (vacancy.userCv != null) Modifier.weight(.25f) else Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (uploadCvLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(42.dp)
                            .padding(end = 12.dp, top = 4.dp),
                        color = DarkGray80
                    )
                } else {
                    if (vacancy.userCv != null) {
                        IconButton(
                            onClick = {
                                if (cvFile != null) downloadCv(cvFile)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Download,
                                contentDescription = stringResource(id = R.string.download_cv),
                                tint = DarkGray80
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            pdfPermissionLauncher.launchPermissionRequest()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = stringResource(id = R.string.update_cv),
                            tint = DarkGray80
                        )
                    }
                }
            }
        }
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
            if (vacancy.statusApply != null) {
                Text(
                    modifier = Modifier
                        .background(
                            color = when (vacancy.statusApply) {
                                ApplicantStatus.ACCEPTED.value -> Green
                                ApplicantStatus.REJECTED.value -> Red
                                else -> DarkGray80
                            }, shape = CircleShape
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    text = when (vacancy.statusApply) {
                        ApplicantStatus.ACCEPTED.value -> stringResource(id = R.string.accepted)
                        ApplicantStatus.REJECTED.value -> stringResource(id = R.string.not_accepted)
                        else -> stringResource(id = R.string.pending)
                    },
                    color = Light
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            applyDialog.value = true
                        },
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
    }
}