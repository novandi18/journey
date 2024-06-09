package com.novandi.core.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandi.core.data.source.local.entity.LatestVacancyEntity

@Dao
interface LatestVacancyDao {
    @Query("SELECT * FROM latest_vacancies")
    fun getAll(): PagingSource<Int, LatestVacancyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vacancies: List<LatestVacancyEntity>)

    @Query("DELETE FROM latest_vacancies")
    fun deleteAll()
}