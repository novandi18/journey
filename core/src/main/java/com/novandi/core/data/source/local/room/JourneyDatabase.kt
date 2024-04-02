package com.novandi.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.novandi.core.data.source.local.dao.AssistantDao
import com.novandi.core.data.source.local.dao.SearchDao
import com.novandi.core.data.source.local.entity.AssistantEntity
import com.novandi.core.data.source.local.entity.SearchEntity

@Database(
    entities = [SearchEntity::class, AssistantEntity::class],
    version = 2,
    exportSchema = false
)
abstract class JourneyDatabase: RoomDatabase() {
    abstract fun searchDao(): SearchDao
    abstract fun assistantDao(): AssistantDao
}