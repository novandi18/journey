package com.novandi.core.data.source.remote.network

import com.novandi.core.data.source.remote.response.RegencyItem
import retrofit2.http.GET
import retrofit2.http.Path

interface RegencyApiService {
    @GET("regencies/{id}.json")
    suspend fun getRegencyById(
        @Path("id") provinceId: String
    ): List<RegencyItem>
}