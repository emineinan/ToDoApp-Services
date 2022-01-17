package com.example.todoapp.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import com.example.todoapp.Constants.Companion.ACTION_STOP_LISTEN
import com.example.todoapp.Constants.Companion.NOTIFICATION_CHANNEL_ID
import com.example.todoapp.Constants.Companion.OPEN_ADD_TODO_TASK_VIEW
import com.example.todoapp.R
import com.example.todoapp.receiver.ToDoReceiver
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.todoapp.Constants.Companion.ADD_TO_DO_TASK_NOTIFICATION_CHANNEL
import com.example.todoapp.R.*
import android.app.NotificationManager

import android.app.NotificationChannel
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.app.ActivityManager
import android.content.Context


class ToDoService : Service() {
    var notification: Notification? = null
    var contentPendingIntent: PendingIntent? = null
    var actionPendingIntent: PendingIntent? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        createIntents()
        createNotification()
        startForeground(1, notification)
    }

    private fun createIntents() {
        val contentIntent = Intent(this, ToDoReceiver::class.java)
        contentIntent.action = OPEN_ADD_TODO_TASK_VIEW
        contentPendingIntent =
            PendingIntent.getBroadcast(this, 1, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val actionIntent = Intent(this, ToDoReceiver::class.java)
        actionIntent.action = ACTION_STOP_LISTEN
        actionPendingIntent =
            PendingIntent.getBroadcast(this, 2, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createNotification() {
        if (notification == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = Notification.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                    .setContentIntent(contentPendingIntent)
                    .setContentTitle(getString(string.title))
                    .addAction(
                        R.drawable.ic_close,
                        getString(R.string.exit),
                        actionPendingIntent
                    )
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_checklist).build()
            } else {
                notification = NotificationCompat.Builder(this, ADD_TO_DO_TASK_NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_checklist)
                    .setContentTitle(getString(R.string.title))
                    .setContentIntent(contentPendingIntent)
                    .addAction(
                        R.drawable.ic_close,
                        getString(R.string.exit),
                        actionPendingIntent
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )

        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(notificationChannel)

        return channelId
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service is started.", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service is stopped.", Toast.LENGTH_SHORT).show()
        stopForeground(true)
    }
}