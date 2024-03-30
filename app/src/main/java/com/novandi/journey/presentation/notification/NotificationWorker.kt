package com.novandi.journey.presentation.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.novandi.core.domain.usecase.JobSeekerUseCase
import com.novandi.journey.HiltApplication
import com.novandi.journey.R
import com.novandi.journey.presentation.main.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    private val jobSeekerUseCase: JobSeekerUseCase,
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    private val notificationId = 17

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val token = inputData.getString(TOKEN)
                val userId = inputData.getString(USER_ID)

                val response = jobSeekerUseCase.getUpdatedJobStatus(token.toString(), userId.toString())
                if (response.isNotEmpty()) {
                    response.forEachIndexed { _, updatedJobStatus ->
                        val text = context.getString(
                            R.string.vacancy_accepted_desc,
                            updatedJobStatus.company,
                            updatedJobStatus.position
                        )
                        showNotification(text)
                    }
                }

                Result.success()
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                Result.failure()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(text: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, HiltApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(context.getString(R.string.vacancy_accepted_channel))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }

    companion object {
        const val TAG = "journey_tag"
        const val TOKEN = "token"
        const val USER_ID = "user_id"
    }
}