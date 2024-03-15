package com.novandi.core.di

import com.novandi.core.domain.interactor.JobProviderInteractor
import com.novandi.core.domain.interactor.JobSeekerInteractor
import com.novandi.core.domain.interactor.RegencyInteractor
import com.novandi.core.domain.interactor.SearchInteractor
import com.novandi.core.domain.interactor.VacancyInteractor
import com.novandi.core.domain.usecase.JobProviderUseCase
import com.novandi.core.domain.usecase.JobSeekerUseCase
import com.novandi.core.domain.usecase.RegencyUseCase
import com.novandi.core.domain.usecase.SearchUseCase
import com.novandi.core.domain.usecase.VacancyUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindJobProviderUseCase(interactor: JobProviderInteractor): JobProviderUseCase

    @Binds
    @Singleton
    abstract fun bindJobSeekerUseCase(interactor: JobSeekerInteractor): JobSeekerUseCase

    @Binds
    @Singleton
    abstract fun bindRegencyUseCase(interactor: RegencyInteractor): RegencyUseCase

    @Binds
    @Singleton
    abstract fun bindVacancyUseCase(interactor: VacancyInteractor): VacancyUseCase

    @Binds
    @Singleton
    abstract fun bindSearchUseCase(interactor: SearchInteractor): SearchUseCase
}