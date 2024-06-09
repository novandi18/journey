package com.novandi.core.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandi.core.data.source.local.entity.AllVacancyEntity

@Dao
interface AllVacancyDao {
    @Query("SELECT * FROM all_vacancies")
    fun getAll(): PagingSource<Int, AllVacancyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vacancies: List<AllVacancyEntity>)

    @Query("DELETE FROM all_vacancies")
    fun deleteAll()
}