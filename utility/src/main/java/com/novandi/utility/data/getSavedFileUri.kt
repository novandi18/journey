package com.novandi.utility.data

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.net.URL

fun getSavedFileUri(fileName: String, fileType: String, fileUrl: String, context: Context): Uri? {
    val mimeType = when(fileType) {
        "PDF" -> "application/pdf"
        else -> ""
    }

    if (mimeType.isEmpty()) return null

    val target = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        fileName
    )
    URL(fileUrl).openStream().use { input ->
        FileOutputStream(target).use { output ->
            input.copyTo(output)
        }
    }

    return target.toUri()
}