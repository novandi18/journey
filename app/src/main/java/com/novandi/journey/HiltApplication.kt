package com.novandi.journey

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Configuration
import com.novandi.utility.consts.NotificationConsts
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication: Application(), Configuration.Provider {
    @Inject lateinit var workerConfiguration: Configuration

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NotificationConsts.CHANNEL_NAME_APPLICANT
            val descriptionText = NotificationConsts.CHANNEL_DESCRIPTION_APPLICANT
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = workerConfiguration

    companion object {
        const val CHANNEL_ID = "journey_id"
    }
}