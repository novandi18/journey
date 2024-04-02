package com.novandi.core.di

import com.novandi.core.data.repository.AssistantRepositoryImpl
import com.novandi.core.data.repository.JobProviderRepositoryImpl
import com.novandi.core.data.repository.JobSeekerRepositoryImpl
import com.novandi.core.data.repository.RegencyRepositoryImpl
import com.novandi.core.data.repository.SearchRepositoryImpl
import com.novandi.core.data.repository.VacancyRepositoryImpl
import com.novandi.core.domain.repository.AssistantRepository
import com.novandi.core.domain.repository.JobProviderRepository
import com.novandi.core.domain.repository.JobSeekerRepository
import com.novandi.core.domain.repository.RegencyRepository
import com.novandi.core.domain.repository.SearchRepository
import com.novandi.core.domain.repository.VacancyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindJobProviderRepository(repository: JobProviderRepositoryImpl): JobProviderRepository

    @Binds
    abstract fun bindJobSeekerRepository(repository: JobSeekerRepositoryImpl): JobSeekerRepository

    @Binds
    abstract fun bindRegencyRepository(repository: RegencyRepositoryImpl): RegencyRepository

    @Binds
    abstract fun bindVacancyRepository(repository: VacancyRepositoryImpl): VacancyRepository

    @Binds
    abstract fun bindSearchRepository(repository: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun bindAssistantRepository(repository: AssistantRepositoryImpl): AssistantRepository
}