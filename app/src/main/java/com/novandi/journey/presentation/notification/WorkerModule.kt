package com.novandi.journey.presentation.notification

import android.content.Context
import androidx.work.Configuration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Singleton
    @Provides
    fun provideWorkManagerConfiguration(
        notificationWorkerFactory: NotificationWorkerFactory
    ): Configuration = Configuration.Builder()
        .setWorkerFactory(notificationWorkerFactory)
        .build()

    @Singleton
    @Provides
    fun provideWorkerStarter(@ApplicationContext context: Context) = WorkerStarter(context)
}