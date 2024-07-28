package com.novandi.core.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.novandi.core.data.source.local.LocalDataSource
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.mapper.VacancyMapper
import com.novandi.utility.data.AppExecutors
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class AllVacanciesRemoteMediator(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val token: String
): RemoteMediator<Int, AllVacancyEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AllVacancyEntity>
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

            val vacancyFeed = remoteDataSource.getVacancies(
                key = token,
                page = loadKey,
                limit = state.config.pageSize
            )

            appExecutors.diskIO().execute {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.deleteAllVacancies()
                }

                val vacancyEntities = VacancyMapper.mapResponseToAllVacancyEntity(vacancyFeed)
                localDataSource.insertAllVacancies(vacancyEntities)
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