package com.novandi.utility.image

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun createCustomTempFile(context: Context): File {
    val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}