package com.novandi.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandi.core.data.source.local.entity.AssistantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssistantDao {
    @Query("SELECT * FROM assistant")
    fun getAll(): Flow<List<AssistantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveChat(chat: AssistantEntity)

    @Query("DELETE FROM assistant")
    fun deleteAll()
}