package com.novandi.journey.presentation.notification

import androidx.work.DelegatingWorkerFactory
import com.novandi.core.domain.usecase.JobSeekerUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationWorkerFactory @Inject constructor(
    jobSeekerUseCase: JobSeekerUseCase
): DelegatingWorkerFactory() {
    init {
        addFactory(JobSeekerWorkerFactory(jobSeekerUseCase))
    }
}