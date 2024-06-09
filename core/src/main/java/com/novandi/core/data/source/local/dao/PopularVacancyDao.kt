package com.novandi.core.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandi.core.data.source.local.entity.PopularVacancyEntity

@Dao
interface PopularVacancyDao {
    @Query("SELECT * FROM popular_vacancies")
    fun getAll(): PagingSource<Int, PopularVacancyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vacancies: List<PopularVacancyEntity>)

    @Query("DELETE FROM popular_vacancies")
    fun deleteAll()
}