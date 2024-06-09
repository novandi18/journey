package com.novandi.utility.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

fun Bitmap.compressImage(context: Context): Bitmap? {
    val imageCropToRect = cropToSquareBitmap(this)
    val imageUri = bitmapToUri(imageCropToRect)
    val imageDrawable = imageUri.toImageDrawable(context)
    val imageCompressed = reduceImageSize(imageDrawable)
    return imageCompressed
}

fun Uri.toImageDrawable(context: Context): Drawable? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(this)
    return Drawable.createFromStream(inputStream, this.toString())
}

fun cropToSquareBitmap(bitmap: Bitmap): Bitmap {
    val size = minOf(bitmap.width, bitmap.height)
    val xOffset = (bitmap.width - size) / 2
    val yOffset = (bitmap.height - size) / 2

    return Bitmap.createBitmap(
        bitmap,
        xOffset,
        yOffset,
        size,
        size
    )
}

fun reduceImageSize(drawable: Drawable?): Bitmap? {
    if (drawable == null) return null

    val baos = ByteArrayOutputStream()
    val bitmap = drawable.toBitmap(800, 800, Bitmap.Config.ARGB_8888)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes: ByteArray = baos.toByteArray()

    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}