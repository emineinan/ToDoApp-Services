package com.example.todoapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.todoapp.R
import com.example.todoapp.receiver.ToDoReceiver

class ToDoService : Service() {

    private var isServiceStarted = false
    private var notificationManager: NotificationManager? = null

    private val builder by lazy {
        NotificationCompat.Builder(  // get an instance of NotificationCompat.Builder
            this,
            this.getString(R.string.todo_notification_channel_id)
        )
            //set title, text and icon to builder
            .setSmallIcon(R.drawable.ic_checklist)
            //.setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(getString(R.string.todo_task_ready))
            .setContentIntent(getPendingIntent())
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    private fun getPendingIntent(): PendingIntent? {
        val resultIntent = Intent(this, ToDoReceiver::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun startForegroundAndShowNotification() {
        createChannel()
        val notification = getNotification("content")
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "to_do"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, channelName, importance
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotification(content: String) = builder.setContentText(content).build()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private companion object {

        private const val CHANNEL_ID = "Channel_ID"
        private const val NOTIFICATION_ID = 777
    }
}