package com.novandi.journey

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.novandi.core.data.source.remote.request.MessagingRegisterRequest
import com.novandi.core.data.store.DataStoreManager
import com.novandi.core.domain.usecase.MessagingUseCase
import com.novandi.journey.presentation.main.MainActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class MessagingService: FirebaseMessagingService() {
    @Inject lateinit var dataStoreManager: DataStoreManager
    @Inject lateinit var messagingUseCase: MessagingUseCase

    override fun onCreate() {
        super.onCreate()
        EntryPointAccessors.fromApplication(
            applicationContext, ServiceEntryPoint::class.java
        ).inject(this)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        GlobalScope.launch {
            dataStoreManager.accountId.collect { accountId ->
                if (!accountId.isNullOrEmpty()) {
                    val fcmRequest = MessagingRegisterRequest(accountId, token)
                    messagingUseCase.registerFcmToken(fcmRequest)
                }
            }
            dataStoreManager.setMessagingToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let { messageData ->
            sendNotification(messageData)
        }
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = this.getString(R.string.notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                this.getString(R.string.notification_name),
                IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        manager.notify(Random.nextInt(), notificationBuilder.build())
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ServiceEntryPoint {
    fun inject(service: MessagingService)
}