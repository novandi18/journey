package com.novandi.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.novandi.core.data.source.local.dao.AllVacancyDao
import com.novandi.core.data.source.local.dao.AssistantDao
import com.novandi.core.data.source.local.dao.LatestVacancyDao
import com.novandi.core.data.source.local.dao.SearchDao
import com.novandi.core.data.source.local.dao.PopularVacancyDao
import com.novandi.core.data.source.local.dao.RecommendationVacancyDao
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.local.entity.AssistantEntity
import com.novandi.core.data.source.local.entity.LatestVacancyEntity
import com.novandi.core.data.source.local.entity.SearchEntity
import com.novandi.core.data.source.local.entity.PopularVacancyEntity
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity

@Database(
    entities = [
        SearchEntity::class,
        AssistantEntity::class,
        RecommendationVacancyEntity::class,
        PopularVacancyEntity::class,
        LatestVacancyEntity::class,
        AllVacancyEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class JourneyDatabase: RoomDatabase() {
    abstract fun searchDao(): SearchDao
    abstract fun assistantDao(): AssistantDao
    abstract fun recommendationVacancyDao(): RecommendationVacancyDao
    abstract fun popularVacancyDao(): PopularVacancyDao
    abstract fun latestVacancyDao(): LatestVacancyDao
    abstract fun allVacancyDao(): AllVacancyDao
}