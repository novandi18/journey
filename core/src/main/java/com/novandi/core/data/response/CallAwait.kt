package com.novandi.core.data.response

import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Call

suspend fun <T> Call<T>.await(timeoutMillis: Long): T? {
    return withTimeoutOrNull(timeoutMillis) {
        return@withTimeoutOrNull try {
            await(timeoutMillis)
        } catch (e: Throwable) {
            null
        }
    }
}