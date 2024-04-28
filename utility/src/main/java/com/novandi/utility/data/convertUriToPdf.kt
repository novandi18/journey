package com.novandi.utility.data

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun convertUriToPdf(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver

    val fileExtension = getFileExtension(context, uri)
    val file = File.createTempFile("temp_cv_file", ".$fileExtension", context.cacheDir)

    try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
    } catch (e: Exception) {
        return null
    }

    return file
}

fun getFileExtension(context: Context, uri: Uri): String? {
    return context.contentResolver.getType(uri)?.let { mimeType ->
        MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }
}