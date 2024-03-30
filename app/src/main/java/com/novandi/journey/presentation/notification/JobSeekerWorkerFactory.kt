package com.novandi.journey.presentation.notification

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.novandi.core.domain.usecase.JobSeekerUseCase

class JobSeekerWorkerFactory(
    private val jobSeekerUseCase: JobSeekerUseCase
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            NotificationWorker::class.java.name ->
                NotificationWorker(jobSeekerUseCase, appContext, workerParameters)
            else -> null
        }
    }
}