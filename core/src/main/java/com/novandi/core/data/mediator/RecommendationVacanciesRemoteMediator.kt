package com.novandi.core.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.novandi.core.data.source.local.LocalDataSource
import com.novandi.core.data.source.local.entity.RecommendationVacancyEntity
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.request.RecommendationVacanciesRequest
import com.novandi.core.mapper.VacancyMapper
import com.novandi.utility.data.AppExecutors
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RecommendationVacanciesRemoteMediator(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val recommendations: RecommendationVacanciesRequest
): RemoteMediator<Int, RecommendationVacancyEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecommendationVacancyEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val vacancyFeed = remoteDataSource.getRecommendationVacancies(
                page = loadKey,
                limit = state.config.pageSize,
                recommendations = recommendations
            )

            appExecutors.diskIO().execute {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.deleteRecommendationVacancies()
                }

                val vacancyEntities = VacancyMapper.mapResponseToRecommendationVacancyEntity(vacancyFeed)
                localDataSource.insertRecommendationVacancies(vacancyEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = vacancyFeed.vacancies.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}