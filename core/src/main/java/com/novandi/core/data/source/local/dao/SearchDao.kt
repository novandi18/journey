package com.novandi.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandi.core.data.source.local.entity.SearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query("SELECT * FROM search ORDER BY id DESC")
    fun getSearch(): Flow<List<SearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSearch(search: SearchEntity)

    @Query("DELETE FROM search WHERE id = :id")
    fun deleteSearch(id: Int)
}