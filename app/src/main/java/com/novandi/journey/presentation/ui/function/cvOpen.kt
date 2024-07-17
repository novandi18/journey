package com.novandi.journey.presentation.ui.function

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.novandi.core.domain.model.File
import com.novandi.journey.BuildConfig

fun cvOpen(
    context: Context,
    file: File
) {
    val intent = Intent(Intent.ACTION_VIEW)
    val uriData = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider",
        file.downloadedUri!!.toUri().toFile()
    )
    intent.setDataAndType(
        uriData,
        "application/pdf"
    )
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    try {
        val activity = context as Activity
        activity.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "Can't open Pdf",
            Toast.LENGTH_SHORT
        ).show()
    }
}