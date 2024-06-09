package com.novandi.core.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity

@Dao
interface RecommendationVacancyDao {
    @Query("SELECT * FROM recommendation_vacancies")
    fun getAll(): PagingSource<Int, RecommendationVacancyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vacancies: List<RecommendationVacancyEntity>)

    @Query("DELETE FROM recommendation_vacancies")
    fun deleteAll()
}