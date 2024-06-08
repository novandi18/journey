package com.novandi.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandi.core.data.source.local.entity.AssistantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssistantDao {
    @Query("SELECT * FROM assistant WHERE user_id = :userId")
    fun getAll(userId: String): Flow<List<AssistantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveChat(chat: AssistantEntity)

    @Query("DELETE FROM assistant WHERE user_id = :userId")
    fun deleteAll(userId: String)

    @Query("DELETE FROM assistant WHERE id = :id")
    fun delete(id: Int)
}