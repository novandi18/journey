package com.novandi.core.data.source.remote.network

import com.novandi.core.BuildConfig
import com.novandi.core.data.source.remote.response.WhatsappResponse
import com.novandi.utility.data.createRequestBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface WhatsappApiService {
    @Multipart
    @POST("create-message")
    suspend fun sendMessage(
        @Part("appkey") appkey: RequestBody = createRequestBody(BuildConfig.WHATSAPP_APP_KEY),
        @Part("authkey") authkey: RequestBody = createRequestBody(BuildConfig.WHATSAPP_AUTH_KEY),
        @Part("to") to: RequestBody,
        @Part("message") message: RequestBody
    ): WhatsappResponse
}