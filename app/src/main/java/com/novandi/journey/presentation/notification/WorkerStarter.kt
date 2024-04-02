package com.novandi.journey.presentation.notification

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.novandi.utility.data.isInternetAvailable
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
class WorkerStarter(
    private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    operator fun invoke(token: String, userId: String) {
        if (
            !isInternetAvailable(context) &&
            token.isEmpty() && userId.isEmpty()
        ) {
            return
        }

        val request = PeriodicWorkRequestBuilder<NotificationWorker>(2, TimeUnit.HOURS)
        request.setInputData(
            workDataOf(
                NotificationWorker.TOKEN to token,
                NotificationWorker.USER_ID to userId
            )
        )
        request.addTag(NotificationWorker.TAG)
        workManager.enqueue(request.build())
    }
}