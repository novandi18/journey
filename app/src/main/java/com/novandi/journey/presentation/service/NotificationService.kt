package com.novandi.journey.presentation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.novandi.journey.presentation.notification.WorkerStarter

class NotificationService: Service() {
    private lateinit var workerStarter: WorkerStarter

    override fun onCreate() {
        super.onCreate()
        workerStarter = WorkerStarter(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = intent?.extras
        if (data != null) {
            val token = data.getString(TOKEN)
            val userId = data.getString(USER_ID)
            if (token != null && userId != null) workerStarter(token, userId)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val TOKEN = "token"
        const val USER_ID = "user_id"
    }
}