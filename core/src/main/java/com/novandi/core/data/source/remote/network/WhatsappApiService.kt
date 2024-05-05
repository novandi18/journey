package com.novandi.core.data.source.remote.network

import com.novandi.core.data.source.remote.request.WhatsappRequest
import com.novandi.core.data.source.remote.response.WhatsappResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface WhatsappApiService {
    @POST("send-message")
    suspend fun sendMessage(
        @Body request: WhatsappRequest
    ): WhatsappResponse
}