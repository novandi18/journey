package com.novandi.core.di

import com.novandi.core.BuildConfig
import com.novandi.core.data.source.remote.network.ApiService
import com.novandi.core.data.source.remote.network.MLApiService
import com.novandi.core.data.source.remote.network.RegencyApiService
import com.novandi.core.data.source.remote.network.WhatsappApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.JOURNEY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRegencyApi(client: OkHttpClient): RegencyApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.REGENCY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(RegencyApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMLApi(client: OkHttpClient): MLApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.ML_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(MLApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideWhatsappApi(client: OkHttpClient): WhatsappApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.WHATSAPP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(WhatsappApiService::class.java)
    }
}