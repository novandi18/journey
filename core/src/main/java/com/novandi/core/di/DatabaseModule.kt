package com.novandi.core.di

import android.content.Context
import androidx.room.Room
import com.novandi.core.consts.Rooms
import com.novandi.core.data.source.local.dao.AllVacancyDao
import com.novandi.core.data.source.local.dao.AssistantDao
import com.novandi.core.data.source.local.dao.LatestVacancyDao
import com.novandi.core.data.source.local.dao.SearchDao
import com.novandi.core.data.source.local.dao.PopularVacancyDao
import com.novandi.core.data.source.local.dao.RecommendationVacancyDao
import com.novandi.core.data.source.local.room.JourneyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): JourneyDatabase =
        Room.databaseBuilder(
            context, JourneyDatabase::class.java, Rooms.JourneyDb
        ).build()

    @Provides
    fun provideSearchDao(database: JourneyDatabase): SearchDao = database.searchDao()

    @Provides
    fun provideAssistantDao(database: JourneyDatabase): AssistantDao = database.assistantDao()

    @Provides
    fun provideRecommendationVacancyDao(database: JourneyDatabase): RecommendationVacancyDao =
        database.recommendationVacancyDao()

    @Provides
    fun providePopularVacancyDao(database: JourneyDatabase): PopularVacancyDao =
        database.popularVacancyDao()

    @Provides
    fun provideLatestVacancyDao(database: JourneyDatabase): LatestVacancyDao =
        database.latestVacancyDao()

    @Provides
    fun provideAllVacancyDao(database: JourneyDatabase): AllVacancyDao = database.allVacancyDao()
}