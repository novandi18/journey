package com.novandi.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.mapper.VacancyMapper

class RecommendationVacanciesPagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val recommendations: RecommendationVacanciesRequest
): PagingSource<Int, Vacancy>() {
    override fun getRefreshKey(state: PagingState<Int, Vacancy>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Vacancy> {
        return try {
            val page = params.key ?: 1
            val response = remoteDataSource.getRecommendationVacancies(
                page, params.loadSize, recommendations
            )
            val result = VacancyMapper.mapResponseToDomain(response)

            LoadResult.Page(
                data = result,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.vacancies.isEmpty()) null else page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}