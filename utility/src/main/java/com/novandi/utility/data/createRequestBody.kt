package com.novandi.utility.data

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun createRequestBody(value: String): RequestBody {
    return value.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}