package com.novandi.core.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.novandi.core.data.source.local.LocalDataSource
import com.novandi.core.data.source.local.entity.AllVacancyEntity
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.utility.data.AppExecutors
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.novandi.core.VacancyDummyData
import com.novandi.core.mapper.VacancyMapper
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.After
import java.io.IOException

class AllVacanciesRemoteMediatorTest {
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource
    @MockK
    private lateinit var localDataSource: LocalDataSource

    private val testAppExecutors = AppExecutors(
        diskIO = testDispatcher.asExecutor(),
        networkIO = testDispatcher.asExecutor(),
        mainThread = testDispatcher.asExecutor()
    )
    private val token = "test_token"

    private lateinit var mediator: AllVacanciesRemoteMediator

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { localDataSource.deleteAllVacancies() } just Runs
        mediator = AllVacanciesRemoteMediator(
            remoteDataSource,
            localDataSource,
            testAppExecutors,
            token
        )
    }

    @After
    fun tearDown() {
        testDispatcher.scheduler.runCurrent()
        testDispatcher.cancel()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `refresh load returns success result when vacancies are fetched successfully`() = runTest {
        val vacancyFeed = VacancyDummyData.get()
        coEvery { remoteDataSource.getVacancies(token, 1, 10) } returns vacancyFeed
        coEvery { localDataSource.insertAllVacancies(any()) } just Runs

        val pagingState = PagingState<Int, AllVacancyEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()

        testDispatcher.scheduler.advanceUntilIdle()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `refresh load returns error result when an exception occurs`() = runTest {
        coEvery { remoteDataSource.getVacancies(any(), any(), any()) } throws IOException()

        val pagingState = PagingState<Int, AllVacancyEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertThat(result is RemoteMediator.MediatorResult.Error).isTrue()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `append load returns success result when vacancies are fetched successfully`() = runTest {
        val vacancyFeed = VacancyDummyData.get()
        coEvery { remoteDataSource.getVacancies(token, 1, 10) } returns vacancyFeed
        coEvery { localDataSource.insertAllVacancies(any()) } just Runs

        val lastItem = VacancyMapper.mapResponseToAllVacancyEntity(vacancyFeed).last()
        val pagingState = PagingState(
            listOf(
                PagingSource.LoadResult.Page(
                    data = listOf(lastItem),
                    prevKey = 1,
                    nextKey = null
                )
            ),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.APPEND, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
        testDispatcher.scheduler.advanceUntilIdle()
    }
}