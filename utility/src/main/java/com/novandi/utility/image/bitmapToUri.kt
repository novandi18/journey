package com.novandi.utility.image

import android.graphics.Bitmap
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun bitmapToUri(image: Bitmap): Uri {
    val tempFile = File.createTempFile("journey_photo", ".png")
    val bytes = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, bytes)
    val bitmapData = bytes.toByteArray()

    val fileOutPut = FileOutputStream(tempFile)
    fileOutPut.write(bitmapData)
    fileOutPut.flush()
    fileOutPut.close()
    return Uri.fromFile(tempFile)
}