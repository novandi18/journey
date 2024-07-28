package com.novandi.core.data.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.novandi.core.VacancyDummyData
import com.novandi.core.data.source.remote.RemoteDataSource
import com.novandi.core.data.source.remote.request.VacanciesSearchRequest
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.novandi.core.domain.model.Vacancy
import com.novandi.core.mapper.VacancyMapper
import io.mockk.mockk
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

class SearchVacanciesPagingSourceTest {
    private val remoteDataSource = mockk<RemoteDataSource>()

    private lateinit var pagingSource: SearchVacanciesPagingSource

    @Before
    fun setup() {
        pagingSource = SearchVacanciesPagingSource(
            remoteDataSource,
            "Android Developer",
            VacanciesSearchRequest()
        )
    }

    @Test
    fun `load returns initial page when LoadParams is Refresh`() = runTest {
        val data = VacancyDummyData.get()
        coEvery { remoteDataSource.getSearchVacancy(any(), 1, any(), any()) } returns data

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assertThat(result is PagingSource.LoadResult.Page).isTrue()
        val page = result as PagingSource.LoadResult.Page<Int, Vacancy>
        val expected = VacancyMapper.mapResponseToDomain(data)
        assertThat(page.data).isEqualTo(expected)
        assertThat(page.prevKey).isNull()
        assertThat(page.nextKey).isEqualTo(2)
    }

    @Test
    fun `Pager emits correct PagingData()`() = runTest {
        val data = VacancyDummyData.get()
        coEvery { remoteDataSource.getSearchVacancy(any(), 1, any(), any()) } returns data

        val pager = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { pagingSource }
        )
        val job = launch {
            pager.flow.collect { pagingData ->
                assertThat(pagingData).isEqualTo(VacancyMapper.mapResponseToDomain(data))
            }
        }

        pagingSource.load(
            PagingSource.LoadParams.Refresh(null, 10, false)
        )
        job.cancelAndJoin()
    }

    @Test
    fun `load appends data when LoadParams is Append`() = runTest {
        val firstData = VacancyDummyData.get(page = 1)
        val secondData = VacancyDummyData.get(page = 2)
        coEvery { remoteDataSource.getSearchVacancy(any(), any(), any(), any()) } returnsMany listOf(
            firstData, secondData
        )

        pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(key = 2, loadSize = 10, placeholdersEnabled = false)
        )

        assertThat(result is PagingSource.LoadResult.Page).isTrue()
        val page = result as PagingSource.LoadResult.Page<Int, Vacancy>
        assertThat(page.data).isEqualTo(VacancyMapper.mapResponseToDomain(secondData))
        assertThat(page.prevKey).isEqualTo(1)
        assertThat(page.nextKey).isEqualTo(3)
    }

    @Test
    fun `load returns Error when exception occurs`() = runTest {
        coEvery { remoteDataSource.getSearchVacancy(any(), any(), any(), any()) } throws Exception("Network Error")
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )
        assertThat(result is PagingSource.LoadResult.Error).isTrue()
        val error = result as PagingSource.LoadResult.Error
        assertThat(error.throwable).isInstanceOf(Exception::class.java)
        assertThat(error.throwable.message).isEqualTo("Network Error")
    }
}