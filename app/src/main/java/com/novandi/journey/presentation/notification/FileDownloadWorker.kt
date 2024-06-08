package com.novandi.journey.presentation.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.novandi.journey.R
import com.novandi.utility.consts.NotificationConsts
import com.novandi.utility.consts.WorkerConsts
import com.novandi.utility.data.getSavedFileUri

class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val fileUrl = inputData.getString(WorkerConsts.KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(WorkerConsts.KEY_FILE_NAME) ?: ""
        val fileType = inputData.getString(WorkerConsts.KEY_FILE_TYPE) ?: ""

        if (
            fileName.isEmpty()
            || fileType.isEmpty()
            || fileUrl.isEmpty()
        ) {
            Result.failure()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NotificationConsts.CHANNEL_NAME_DOWNLOAD
            val description = NotificationConsts.CHANNEL_DESCRIPTION_DOWNLOAD
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConsts.CHANNEL_ID_DOWNLOAD, name, importance)
            channel.description = description

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context,NotificationConsts.CHANNEL_ID_DOWNLOAD)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.downloading))
            .setOngoing(true)
            .setProgress(0,0,true)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Result.failure()
        }

        NotificationManagerCompat.from(context).notify(NotificationConsts.NOTIFICATION_ID_DOWNLOAD, builder.build())

        val uri = getSavedFileUri(
            fileName = fileName,
            fileType = fileType,
            fileUrl = fileUrl,
            context = context
        )

        NotificationManagerCompat.from(context).cancel(NotificationConsts.NOTIFICATION_ID_DOWNLOAD)
        return if (uri != null) {
            Result.success(workDataOf(WorkerConsts.KEY_FILE_URI to uri.toString()))
        } else {
            Result.failure()
        }
    }
}